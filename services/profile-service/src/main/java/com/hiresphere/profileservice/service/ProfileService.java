package com.hiresphere.profileservice.service;

import com.hiresphere.profileservice.model.Profile;
import com.hiresphere.profileservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Service @RequiredArgsConstructor @Slf4j
public class ProfileService {

    private final ProfileRepository repository;

    public Optional<Profile> getProfile(String userId) {
        return repository.findById(userId);
    }

    public Profile createProfile(Profile profile) {
        if (repository.findById(profile.getUserId()).isPresent())
            throw new IllegalStateException("Profile already exists: " + profile.getUserId());
        return repository.save(profile);
    }

    public Profile updateProfile(Profile profile) {
        repository.findById(profile.getUserId())
            .orElseThrow(() -> new NoSuchElementException("Profile not found: " + profile.getUserId()));
        return repository.save(profile);
    }

    public List<Profile> searchInterviewers(String domain, String interviewType,
                                             Integer minExp, Float minRating, Integer maxPrice) {
        return repository.findAllInterviewers().stream()
            .filter(p -> domain == null || containsIgnoreCase(p.getDomains(), domain))
            .filter(p -> interviewType == null || containsIgnoreCase(p.getInterviewTypes(), interviewType))
            .filter(p -> minExp == null || (p.getYearsExperience() != null && p.getYearsExperience() >= minExp))
            .filter(p -> minRating == null || (p.getRating() != null && p.getRating() >= minRating))
            .filter(p -> maxPrice == null || (p.getPricePerHour() != null && p.getPricePerHour() <= maxPrice))
            .toList();
    }

    public void updateRating(String userId, float newRating, int reviewCount) {
        repository.findById(userId).ifPresent(profile -> {
            profile.setRating(newRating);
            profile.setTotalReviews(reviewCount);
            repository.save(profile);
        });
    }

    private boolean containsIgnoreCase(List<String> list, String value) {
        return list != null && list.stream().anyMatch(v -> v.equalsIgnoreCase(value));
    }
}
