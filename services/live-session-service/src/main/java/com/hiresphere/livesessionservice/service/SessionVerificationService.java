package com.hiresphere.livesessionservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

/**
 * Verifies a booking is CONFIRMED before allowing a live session to start.
 * Calls the Booking Service REST endpoint (gRPC stub can replace this later).
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SessionVerificationService {

    private final RestTemplate restTemplate;

    @Value("${booking.service.url:http://localhost:8083}")
    private String bookingServiceUrl;

    /**
     * Returns true if the booking exists, is CONFIRMED, and the userId
     * is either the candidate or the interviewer on that booking.
     */
    public boolean verifySession(String bookingId, String userId) {
        try {
            String url = bookingServiceUrl + "/bookings/" + bookingId;
            Map booking = restTemplate.getForObject(url, Map.class);
            if (booking == null) return false;

            String status       = (String) booking.get("status");
            String candidateId  = (String) booking.get("candidateId");
            String interviewerId = (String) booking.get("interviewerId");

            boolean confirmed  = "CONFIRMED".equals(status);
            boolean authorised = userId.equals(candidateId) || userId.equals(interviewerId);

            if (!confirmed)  log.warn("Session denied — booking {} not CONFIRMED (status={})", bookingId, status);
            if (!authorised) log.warn("Session denied — user {} not on booking {}", userId, bookingId);

            return confirmed && authorised;

        } catch (Exception e) {
            // Circuit breaker: if Booking Service is down, deny the session
            log.error("Failed to verify booking {}: {}", bookingId, e.getMessage());
            return false;
        }
    }
}
