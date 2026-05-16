package com.hiresphere.submissionservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiresphere.submissionservice.model.Submission;
import com.hiresphere.submissionservice.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import java.time.Duration;
import java.util.*;

@Service @RequiredArgsConstructor @Slf4j
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${s3.bucket-name}")            private String bucketName;
    @Value("${sqs.github-analysis-queue}") private String githubQueueUrl;

    /**
     * Generates a pre-signed S3 PUT URL valid for 15 minutes.
     * The frontend uploads directly to S3 — this pod never touches the file bytes.
     */
    public Map<String, String> generateUploadUrl(String candidateId, String bookingId, String filename) {
        String s3Key = String.format("submissions/%s/%s/%s", candidateId, bookingId, filename);

        var presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(15))
            .putObjectRequest(r -> r.bucket(bucketName).key(s3Key)
                .contentType("application/octet-stream"))
            .build();

        String uploadUrl = s3Presigner.presignPutObject(presignRequest).url().toString();

        // Create a pending submission record
        Submission submission = Submission.builder()
            .candidateId(candidateId)
            .bookingId(bookingId)
            .type(Submission.SubmissionType.FILE_UPLOAD)
            .s3Key(s3Key)
            .status(Submission.SubmissionStatus.PENDING)
            .build();
        submissionRepository.save(submission);

        return Map.of("uploadUrl", uploadUrl, "s3Key", s3Key, "submissionId", submission.getId());
    }

    /**
     * Submits a GitHub URL — queues the analysis job to SQS Lambda worker.
     */
    @Transactional
    public Submission submitGithubUrl(String candidateId, String bookingId, String githubUrl) {
        Submission submission = Submission.builder()
            .candidateId(candidateId)
            .bookingId(bookingId)
            .type(Submission.SubmissionType.GITHUB_URL)
            .githubUrl(githubUrl)
            .status(Submission.SubmissionStatus.ANALYSING)
            .build();
        submission = submissionRepository.save(submission);

        // Queue the analysis job to SQS — Lambda worker picks this up
        try {
            Map<String, Object> job = Map.of(
                "submissionId", submission.getId(),
                "githubUrl",    githubUrl,
                "candidateId",  candidateId
            );
            sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(githubQueueUrl)
                .messageBody(objectMapper.writeValueAsString(job))
                .build());
            log.info("Queued GitHub analysis for submissionId={}", submission.getId());
        } catch (Exception e) {
            log.error("Failed to queue GitHub analysis: {}", e.getMessage());
        }
        return submission;
    }

    /** Called by Lambda via webhook after analysis completes */
    @Transactional
    public Submission updateAnalysisResult(String submissionId, String analysisJson) {
        Submission submission = submissionRepository.findById(submissionId)
            .orElseThrow(() -> new NoSuchElementException("Submission not found: " + submissionId));
        submission.setGithubAnalysis(analysisJson);
        submission.setStatus(Submission.SubmissionStatus.REVIEWED);
        return submissionRepository.save(submission);
    }

    public List<Submission> getCandidateSubmissions(String candidateId) {
        return submissionRepository.findByCandidateIdOrderByCreatedAtDesc(candidateId);
    }

    public Optional<Submission> getSubmission(String submissionId) {
        return submissionRepository.findById(submissionId);
    }
}
