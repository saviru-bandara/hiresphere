package com.hiresphere.bookingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiresphere.bookingservice.model.Booking;
import com.hiresphere.bookingservice.model.Booking.BookingStatus;
import com.hiresphere.bookingservice.repository.BookingRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import java.time.Instant;
import java.util.*;

@Service @RequiredArgsConstructor @Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Value("${sqs.booking-events-queue-url}")
    private String bookingEventsQueueUrl;

    /**
     * Creates a Stripe PaymentIntent for the booking amount.
     * Returns the client_secret to the frontend for payment confirmation.
     */
    public Map<String, String> createPaymentIntent(String candidateId, String interviewerId,
                                                    String slotId, double amount) throws Exception {
        // Check slot is not already booked
        if (bookingRepository.existsByInterviewerIdAndSlotIdAndStatusNot(
                interviewerId, slotId, BookingStatus.CANCELLED)) {
            throw new IllegalStateException("Slot is already booked");
        }

        Stripe.apiKey = stripeSecretKey;
        PaymentIntent intent = PaymentIntent.create(
            PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))   // Stripe uses cents
                .setCurrency("usd")
                .putMetadata("candidateId",   candidateId)
                .putMetadata("interviewerId", interviewerId)
                .putMetadata("slotId",        slotId)
                .build()
        );
        return Map.of("clientSecret", intent.getClientSecret(), "paymentIntentId", intent.getId());
    }

    /**
     * Called after Stripe webhook confirms payment_intent.succeeded.
     * Creates the booking record and fires the SQS confirmation event.
     */
    @Transactional
    public Booking confirmBooking(String paymentIntentId, String candidateId,
                                   String interviewerId, String slotId,
                                   Instant startTime, Instant endTime, double amount) {
        Booking booking = Booking.builder()
            .candidateId(candidateId)
            .interviewerId(interviewerId)
            .slotId(slotId)
            .startTime(startTime)
            .endTime(endTime)
            .status(BookingStatus.CONFIRMED)
            .amountPaid(amount)
            .stripePaymentIntentId(paymentIntentId)
            .sessionToken(UUID.randomUUID().toString())
            .build();

        booking = bookingRepository.save(booking);
        publishBookingConfirmedEvent(booking);
        return booking;
    }

    public List<Booking> getCandidateBookings(String candidateId) {
        return bookingRepository.findByCandidateIdOrderByCreatedAtDesc(candidateId);
    }

    public List<Booking> getInterviewerBookings(String interviewerId) {
        return bookingRepository.findByInterviewerIdOrderByCreatedAtDesc(interviewerId);
    }

    public Optional<Booking> getBooking(String bookingId) {
        return bookingRepository.findById(bookingId);
    }

    @Transactional
    public Booking cancelBooking(String bookingId, String userId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new NoSuchElementException("Booking not found"));
        if (!booking.getCandidateId().equals(userId) && !booking.getInterviewerId().equals(userId))
            throw new SecurityException("Not authorised to cancel this booking");
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    private void publishBookingConfirmedEvent(Booking booking) {
        try {
            Map<String, Object> event = Map.of(
                "eventType",    "BOOKING_CONFIRMED",
                "bookingId",    booking.getId(),
                "candidateId",  booking.getCandidateId(),
                "interviewerId", booking.getInterviewerId(),
                "startTime",    booking.getStartTime().toString(),
                "amount",       booking.getAmountPaid()
            );
            sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(bookingEventsQueueUrl)
                .messageBody(objectMapper.writeValueAsString(event))
                .build());
            log.info("Published booking confirmed event for bookingId={}", booking.getId());
        } catch (Exception e) {
            // Non-critical — booking is already saved; notification will retry via DLQ
            log.error("Failed to publish booking event: {}", e.getMessage());
        }
    }
}
