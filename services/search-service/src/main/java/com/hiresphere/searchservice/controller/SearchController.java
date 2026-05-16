package com.hiresphere.searchservice.controller;

import com.hiresphere.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/search") @RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * Search interviewers with optional filters.
     * Results served from Redis cache where available.
     * Cache miss -> delegates to Profile Service gRPC.
     */
    @GetMapping("/interviewers")
    public ResponseEntity<List<Map<String, Object>>> searchInterviewers(
            @RequestParam(required = false) String domain,
            @RequestParam(required = false) String interviewType,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(
            searchService.searchInterviewers(domain, interviewType, minExperience, minRating, maxPrice, page, size)
        );
    }

    @GetMapping("/interviewers/{userId}")
    public ResponseEntity<Map<String, Object>> getInterviewer(@PathVariable String userId) {
        return searchService.getInterviewerProfile(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
