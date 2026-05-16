package com.hiresphere.bookingservice.repository;

import com.hiresphere.bookingservice.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByCandidateIdOrderByCreatedAtDesc(String candidateId);
    List<Booking> findByInterviewerIdOrderByCreatedAtDesc(String interviewerId);
    List<Booking> findByInterviewerIdAndStatus(String interviewerId, Booking.BookingStatus status);
    boolean existsByInterviewerIdAndSlotIdAndStatusNot(String interviewerId, String slotId, Booking.BookingStatus status);
}
