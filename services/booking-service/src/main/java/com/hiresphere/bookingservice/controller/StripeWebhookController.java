package com.hiresphere.bookingservice.controller;

import com.hiresphere.bookingservice.service.BookingService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;

/**
 * Receives Stripe webhook events.
 *
 * In Stripe Dashboard, set the webhook endpoint to:
 *   https://<your-domain>/api/bookings/webhook/stripe
 *
 * Events handled:
 *   payment_intent.succeeded  → confirms the booking and fires SQS event
 *   payment_intent.failed     → logs the failure (booking stays PENDING)
 */
@RestController
@RequestMapping("/bookings/webhook")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final BookingService bookingService;

    @Value("${stripe.webhook-secret:whsec_placeholder}")
    private String webhookSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            log.error("Stripe webhook signature verification failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        log.info("Received Stripe event: type={}, id={}", event.getType(), event.getId());

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElseThrow();

            String candidateId   = intent.getMetadata().get("candidateId");
            String interviewerId = intent.getMetadata().get("interviewerId");
            String slotId        = intent.getMetadata().get("slotId");
            String startTimeStr  = intent.getMetadata().get("startTime");
            String endTimeStr    = intent.getMetadata().get("endTime");
            double amount        = intent.getAmount() / 100.0;

            bookingService.confirmBooking(
                intent.getId(),
                candidateId,
                interviewerId,
                slotId,
                startTimeStr != null ? Instant.parse(startTimeStr) : Instant.now(),
                endTimeStr   != null ? Instant.parse(endTimeStr)   : Instant.now().plusSeconds(3600),
                amount
            );

            log.info("Booking confirmed for paymentIntentId={}", intent.getId());

        } else if ("payment_intent.payment_failed".equals(event.getType())) {
            PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                .getObject().orElseThrow();
            log.warn("Payment failed for intentId={}", intent.getId());
        }

        return ResponseEntity.ok("received");
    }
}
