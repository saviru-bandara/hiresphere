package com.hiresphere.profileservice.grpc;

import com.hiresphere.profile.grpc.*;
import com.hiresphere.profileservice.model.Profile;
import com.hiresphere.profileservice.service.ProfileService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.List;
import java.util.stream.Collectors;

/**
 * gRPC server — exposes Profile operations to Search and other services.
 * Called internally by Search Service for interviewer lookups.
 */
@GrpcService
@RequiredArgsConstructor
@Slf4j
public class ProfileGrpcService extends ProfileServiceGrpc.ProfileServiceImplBase {

    private final ProfileService profileService;

    @Override
    public void getProfile(GetProfileRequest request,
                            StreamObserver<GetProfileResponse> responseObserver) {
        log.debug("gRPC getProfile: userId={}", request.getUserId());

        profileService.getProfile(request.getUserId()).ifPresentOrElse(
            p -> responseObserver.onNext(GetProfileResponse.newBuilder()
                .setProfile(toGrpc(p)).setFound(true).build()),
            () -> responseObserver.onNext(GetProfileResponse.newBuilder()
                .setFound(false).build())
        );
        responseObserver.onCompleted();
    }

    @Override
    public void searchInterviewers(SearchInterviewersRequest request,
                                    StreamObserver<SearchInterviewersResponse> responseObserver) {
        log.debug("gRPC searchInterviewers: domain={}, type={}", request.getDomain(), request.getInterviewType());

        List<Profile> results = profileService.searchInterviewers(
            request.getDomain().isBlank()   ? null : request.getDomain(),
            request.getInterviewType() == InterviewType.SYSTEM_DESIGN ? null : request.getInterviewType().name(),
            request.getMinExperience() == 0 ? null : request.getMinExperience(),
            request.getMinRating()     == 0 ? null : request.getMinRating(),
            request.getMaxPrice()      == 0 ? null : request.getMaxPrice()
        );

        SearchInterviewersResponse response = SearchInterviewersResponse.newBuilder()
            .addAllInterviewers(results.stream().map(this::toGrpc).collect(Collectors.toList()))
            .setTotal(results.size())
            .setPage(request.getPage())
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateRating(UpdateRatingRequest request,
                              StreamObserver<UpdateRatingResponse> responseObserver) {
        profileService.updateRating(request.getUserId(), request.getNewRating(), request.getReviewCount());
        responseObserver.onNext(UpdateRatingResponse.newBuilder().setSuccess(true).build());
        responseObserver.onCompleted();
    }

    // ── Mapper: domain Profile → gRPC Profile ──────────────────────────────
    private com.hiresphere.profile.grpc.Profile toGrpc(Profile p) {
        var builder = com.hiresphere.profile.grpc.Profile.newBuilder()
            .setUserId(p.getUserId())
            .setName(p.getName() != null ? p.getName() : "")
            .setEmail(p.getEmail() != null ? p.getEmail() : "")
            .setBio(p.getBio() != null ? p.getBio() : "")
            .setRating(p.getRating() != null ? p.getRating() : 0f)
            .setTotalReviews(p.getTotalReviews() != null ? p.getTotalReviews() : 0)
            .setYearsExperience(p.getYearsExperience() != null ? p.getYearsExperience() : 0)
            .setPricePerHour(p.getPricePerHour() != null ? p.getPricePerHour() : 0)
            .setAvatarUrl(p.getAvatarUrl() != null ? p.getAvatarUrl() : "")
            .setLinkedinUrl(p.getLinkedinUrl() != null ? p.getLinkedinUrl() : "")
            .setGithubUrl(p.getGithubUrl() != null ? p.getGithubUrl() : "")
            .setCreatedAt(p.getCreatedAt() != null ? p.getCreatedAt() : 0L)
            .setUpdatedAt(p.getUpdatedAt() != null ? p.getUpdatedAt() : 0L);

        if (p.getDomains() != null)       builder.addAllDomains(p.getDomains());
        if (p.getBadges() != null)        builder.addAllBadges(p.getBadges());
        if (p.getInterviewTypes() != null)
            p.getInterviewTypes().forEach(t -> {
                try { builder.addInterviewTypes(InterviewType.valueOf(t)); }
                catch (Exception ignored) {}
            });

        return builder.build();
    }
}
