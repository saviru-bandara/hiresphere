package com.hiresphere.submissionservice.repository;
import com.hiresphere.submissionservice.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SubmissionRepository extends JpaRepository<Submission, String> {
    List<Submission> findByCandidateIdOrderByCreatedAtDesc(String candidateId);
    List<Submission> findByBookingId(String bookingId);
}
