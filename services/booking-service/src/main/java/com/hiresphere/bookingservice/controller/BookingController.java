package com.hiresphere.bookingservice.controller;

import com.hiresphere.bookingservice.model.Booking;
import com.hiresphere.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /** Step 1: Frontend calls this to get a Stripe PaymentIntent client secret */
    @PostMapping("/payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(
            @RequestHeader("X-User-Id") String candidateId,
            @RequestBody Map<String, Object> request) throws Exception {
        return ResponseEntity.ok(bookingService.createPaymentIntent(
            candidateId,
            (String) request.get("interviewerId"),
            (String) request.get("slotId"),
            ((Number) request.get("amount")).doubleValue()
        ));
    }

    /** Step 2: Called by Stripe webhook after payment_intent.succeeded */
    @PostMapping("/confirm")
    public ResponseEntity<Booking> confirmBooking(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(bookingService.confirmBooking(
            (String) request.get("paymentIntentId"),
            (String) request.get("candidateId"),
            (String) request.get("interviewerId"),
            (String) request.get("slotId"),
            Instant.parse((String) request.get("startTime")),
            Instant.parse((String) request.get("endTime")),
            ((Number) request.get("amount")).doubleValue()
        ));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getMyBookings(
            @RequestHeader("X-User-Id") String userId,
            @RequestHeader("X-User-Role") String role) {
        List<Booking> bookings = "INTERVIEWER".equals(role)
            ? bookingService.getInterviewerBookings(userId)
            : bookingService.getCandidateBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable String bookingId) {
        return bookingService.getBooking(bookingId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable String bookingId,
                                                  @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId, userId));
    }
}
