package com.hiresphere.profileservice.controller;

import com.hiresphere.profileservice.model.Profile;
import com.hiresphere.profileservice.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<Profile> getMyProfile(@RequestHeader("X-User-Id") String userId) {
        return profileService.getProfile(userId).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfile(@PathVariable String userId) {
        return profileService.getProfile(userId).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Profile> createProfile(@RequestBody Profile profile,
                                                  @RequestHeader("X-User-Id") String userId) {
        profile.setUserId(userId);
        return ResponseEntity.ok(profileService.createProfile(profile));
    }

    @PutMapping("/me")
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile profile,
                                                  @RequestHeader("X-User-Id") String userId) {
        profile.setUserId(userId);
        return ResponseEntity.ok(profileService.updateProfile(profile));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Profile>> search(
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) String interviewType,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Integer maxPrice) {
        return ResponseEntity.ok(
            profileService.searchInterviewers(domain, interviewType, minExperience, minRating, maxPrice)
        );
    }
}
