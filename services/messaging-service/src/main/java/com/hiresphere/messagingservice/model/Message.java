package com.hiresphere.messagingservice.model;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@DynamoDbBean
public class Message {
    private String conversationId;  // PK: "userId1#userId2" (sorted alphabetically)
    private String timestamp;       // SK: ISO-8601 timestamp
    private String messageId;
    private String senderId;
    private String recipientId;
    private String content;
    private boolean read;
    private Long createdAt;

    @DynamoDbPartitionKey
    public String getConversationId() { return conversationId; }

    @DynamoDbSortKey
    public String getTimestamp() { return timestamp; }

    /** Deterministic conversation ID — always same regardless of who initiates */
    public static String buildConversationId(String userA, String userB) {
        return userA.compareTo(userB) < 0
            ? userA + "#" + userB
            : userB + "#" + userA;
    }
}
