package com.hiresphere.messagingservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiresphere.messagingservice.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor @Slf4j
public class MessagingService {

    private final DynamoDbEnhancedClient dynamoClient;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    @Value("${dynamodb.messages-table:hiresphere-messages}")
    private String tableName;

    @Value("${sqs.notification-queue-url}")
    private String notificationQueueUrl;

    private DynamoDbTable<Message> table() {
        return dynamoClient.table(tableName, TableSchema.fromBean(Message.class));
    }

    public Message sendMessage(String senderId, String recipientId, String content) {
        String conversationId = Message.buildConversationId(senderId, recipientId);
        String timestamp = Instant.now().toString();

        Message message = Message.builder()
            .conversationId(conversationId)
            .timestamp(timestamp)
            .messageId(UUID.randomUUID().toString())
            .senderId(senderId)
            .recipientId(recipientId)
            .content(content)
            .read(false)
            .createdAt(System.currentTimeMillis())
            .build();

        table().putItem(message);
        publishNotificationEvent(message);
        return message;
    }

    public List<Message> getConversation(String userA, String userB, int limit) {
        String conversationId = Message.buildConversationId(userA, userB);
        QueryEnhancedRequest query = QueryEnhancedRequest.builder()
            .queryConditional(QueryConditional.keyEqualTo(
                Key.builder().partitionValue(conversationId).build()))
            .limit(limit)
            .scanIndexForward(false)  // newest first
            .build();
        return table().query(query).items().stream().collect(Collectors.toList());
    }

    private void publishNotificationEvent(Message message) {
        try {
            Map<String, Object> event = Map.of(
                "eventType",   "NEW_MESSAGE",
                "messageId",   message.getMessageId(),
                "senderId",    message.getSenderId(),
                "recipientId", message.getRecipientId(),
                "preview",     message.getContent().substring(0, Math.min(50, message.getContent().length()))
            );
            sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(notificationQueueUrl)
                .messageBody(objectMapper.writeValueAsString(event))
                .build());
        } catch (Exception e) {
            log.warn("Failed to publish message notification: {}", e.getMessage());
        }
    }
}
