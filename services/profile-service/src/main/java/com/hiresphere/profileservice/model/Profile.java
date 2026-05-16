package com.hiresphere.profileservice.model;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@DynamoDbBean
public class Profile {
    private String userId;
    private String name;
    private String email;
    private String profileType;    // CANDIDATE | INTERVIEWER
    private String bio;
    private List<String> domains;
    private List<String> badges;
    private Float rating;
    private Integer totalReviews;
    private Integer yearsExperience;
    private List<String> interviewTypes;
    private String avatarUrl;
    private Integer pricePerHour;
    private String linkedinUrl;
    private String githubUrl;
    private Long createdAt;
    private Long updatedAt;

    @DynamoDbPartitionKey
    public String getUserId() { return userId; }
}
