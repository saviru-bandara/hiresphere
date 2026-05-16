package com.hiresphere.livesessionservice.handler;

import com.hiresphere.livesessionservice.service.SessionVerificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebRTC Signaling Handler.
 *
 * Message types handled:
 *   JOIN        — peer joins a session room (identified by bookingId)
 *   OFFER       — SDP offer from initiating peer
 *   ANSWER      — SDP answer from receiving peer
 *   ICE_CANDIDATE — ICE candidate for NAT traversal
 *   LEAVE       — peer leaves the session
 *   WHITEBOARD  — whiteboard draw event forwarded to peer
 */
@Component @RequiredArgsConstructor @Slf4j
public class SignalingHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final SessionVerificationService verificationService;

    // bookingId -> Map<userId, WebSocketSession>
    private final Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket connection established: sessionId={}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonNode payload = objectMapper.readTree(message.getPayload());
        String type      = payload.get("type").asText();
        String bookingId = payload.get("bookingId").asText();
        String userId    = payload.get("userId").asText();

        switch (type) {
            case "JOIN"           -> handleJoin(session, bookingId, userId, payload);
            case "OFFER"          -> relay(bookingId, userId, message);
            case "ANSWER"         -> relay(bookingId, userId, message);
            case "ICE_CANDIDATE"  -> relay(bookingId, userId, message);
            case "WHITEBOARD"     -> relay(bookingId, userId, message);
            case "LEAVE"          -> handleLeave(bookingId, userId);
            default               -> log.warn("Unknown signaling message type: {}", type);
        }
    }

    private void handleJoin(WebSocketSession session, String bookingId,
                             String userId, JsonNode payload) throws Exception {
        // Verify booking is confirmed and user is a participant before allowing join
        if (!verificationService.verifySession(bookingId, userId)) {
            log.warn("Rejected JOIN for bookingId={} userId={} — not authorised", bookingId, userId);
            Map<String, String> denied = Map.of("type", "SESSION_DENIED",
                "reason", "Booking not confirmed or user not authorised");
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(denied)));
            session.close();
            return;
        }

        rooms.computeIfAbsent(bookingId, k -> new ConcurrentHashMap<>()).put(userId, session);
        log.info("User {} joined session for bookingId={}", userId, bookingId);

        // Notify the other peer that someone joined
        Map<String, String> joined = Map.of("type", "PEER_JOINED", "userId", userId, "bookingId", bookingId);
        relay(bookingId, userId, new TextMessage(objectMapper.writeValueAsString(joined)));
    }

    private void handleLeave(String bookingId, String userId) throws Exception {
        Map<String, WebSocketSession> room = rooms.get(bookingId);
        if (room != null) {
            room.remove(userId);
            if (room.isEmpty()) rooms.remove(bookingId);
        }
        Map<String, String> left = Map.of("type", "PEER_LEFT", "userId", userId, "bookingId", bookingId);
        relay(bookingId, userId, new TextMessage(objectMapper.writeValueAsString(left)));
    }

    /** Forwards a message to all OTHER peers in the room */
    private void relay(String bookingId, String senderUserId, TextMessage message) {
        Map<String, WebSocketSession> room = rooms.get(bookingId);
        if (room == null) return;
        room.forEach((uid, ws) -> {
            if (!uid.equals(senderUserId) && ws.isOpen()) {
                try { ws.sendMessage(message); }
                catch (Exception e) { log.error("Failed to relay to {}: {}", uid, e.getMessage()); }
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Clean up all rooms this session was part of
        rooms.forEach((bookingId, room) ->
            room.entrySet().removeIf(entry -> entry.getValue().getId().equals(session.getId()))
        );
        rooms.entrySet().removeIf(entry -> entry.getValue().isEmpty());
        log.info("WebSocket closed: sessionId={}, status={}", session.getId(), status);
    }
}
