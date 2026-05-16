package com.hiresphere.profileservice.repository;

import com.hiresphere.profileservice.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProfileRepository {

    private final DynamoDbEnhancedClient dynamoClient;

    private DynamoDbTable<Profile> table() {
        String tableName = System.getenv().getOrDefault("DYNAMODB_PROFILES_TABLE", "hiresphere-profiles");
        return dynamoClient.table(tableName, TableSchema.fromBean(Profile.class));
    }

    public Optional<Profile> findById(String userId) {
        return Optional.ofNullable(
            table().getItem(Key.builder().partitionValue(userId).build())
        );
    }

    public Profile save(Profile profile) {
        long now = System.currentTimeMillis();
        profile.setUpdatedAt(now);
        if (profile.getCreatedAt() == null) profile.setCreatedAt(now);
        table().putItem(profile);
        return profile;
    }

    public void delete(String userId) {
        table().deleteItem(Key.builder().partitionValue(userId).build());
    }

    /** Scan for all interviewers — in production use a GSI on profileType */
    public List<Profile> findAllInterviewers() {
        return table().scan().items().stream()
            .filter(p -> "INTERVIEWER".equals(p.getProfileType()))
            .collect(Collectors.toList());
    }
}
