package com.hiresphere.messagingservice.controller;

import com.hiresphere.messagingservice.model.Message;
import com.hiresphere.messagingservice.service.MessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/messages") @RequiredArgsConstructor
public class MessagingController {

    private final MessagingService messagingService;

    @PostMapping("/send")
    public ResponseEntity<Message> send(@RequestHeader("X-User-Id") String senderId,
                                         @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(
            messagingService.sendMessage(senderId, body.get("recipientId"), body.get("content")));
    }

    @GetMapping("/conversation/{otherUserId}")
    public ResponseEntity<List<Message>> getConversation(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable String otherUserId,
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(messagingService.getConversation(userId, otherUserId, limit));
    }
}
