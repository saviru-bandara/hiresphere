package com.hiresphere.submissionservice.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name = "submissions")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Submission {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false) private String candidateId;
    @Column(nullable = false) private String bookingId;

    @Enumerated(EnumType.STRING)
    private SubmissionType type;   // FILE_UPLOAD | GITHUB_URL

    private String s3Key;          // Set for FILE_UPLOAD
    private String githubUrl;      // Set for GITHUB_URL
    private String githubAnalysis; // JSON blob from Lambda analysis

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    private String evaluationReportS3Key;
    private String evaluatorFeedback;
    private Integer score;

    @Column(updatable = false) private Instant createdAt;
    private Instant updatedAt;

    @PrePersist  protected void onCreate() { createdAt = updatedAt = Instant.now(); if (status == null) status = SubmissionStatus.PENDING; }
    @PreUpdate   protected void onUpdate() { updatedAt = Instant.now(); }

    public enum SubmissionType   { FILE_UPLOAD, GITHUB_URL }
    public enum SubmissionStatus { PENDING, ANALYSING, REVIEWED, EVALUATED }
}
