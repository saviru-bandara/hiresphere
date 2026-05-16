package com.hiresphere.submissionservice.controller;

import com.hiresphere.submissionservice.model.Submission;
import com.hiresphere.submissionservice.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/submissions") @RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    /** Returns a pre-signed S3 URL — frontend uploads directly to S3 */
    @PostMapping("/upload-url")
    public ResponseEntity<Map<String, String>> getUploadUrl(
            @RequestHeader("X-User-Id") String candidateId,
            @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(submissionService.generateUploadUrl(
            candidateId, request.get("bookingId"), request.get("filename")));
    }

    /** Submit a GitHub repository URL for analysis */
    @PostMapping("/github")
    public ResponseEntity<Submission> submitGithub(
            @RequestHeader("X-User-Id") String candidateId,
            @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(submissionService.submitGithubUrl(
            candidateId, request.get("bookingId"), request.get("githubUrl")));
    }

    /** Webhook called by GitHub analysis Lambda when analysis completes */
    @PostMapping("/webhook/analysis-complete")
    public ResponseEntity<Submission> analysisComplete(@RequestBody Map<String, String> payload) {
        return ResponseEntity.ok(submissionService.updateAnalysisResult(
            payload.get("submissionId"), payload.get("analysis")));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Submission>> mySubmissions(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(submissionService.getCandidateSubmissions(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Submission> getSubmission(@PathVariable String id) {
        return submissionService.getSubmission(id)
            .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
