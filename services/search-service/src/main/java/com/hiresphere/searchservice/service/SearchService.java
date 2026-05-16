package com.hiresphere.searchservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
import java.util.*;

@Service @RequiredArgsConstructor @Slf4j
public class SearchService {

    private final StringRedisTemplate redis;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${services.profile.url:http://profile-service:8081}")
    private String profileServiceUrl;

    private static final Duration CACHE_TTL = Duration.ofSeconds(60);

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> searchInterviewers(String domain, String interviewType,
            Integer minExp, Float minRating, Integer maxPrice, int page, int size) {

        String cacheKey = buildCacheKey(domain, interviewType, minExp, minRating, maxPrice, page, size);

        // Try cache first
        String cached = redis.opsForValue().get(cacheKey);
        if (cached != null) {
            log.debug("Cache hit for key={}", cacheKey);
            try { return objectMapper.readValue(cached, new TypeReference<>() {}); }
            catch (Exception e) { log.warn("Cache parse error, falling through to profile service"); }
        }

        // Cache miss — call Profile Service
        String url = profileServiceUrl + "/profiles/search?" + buildQueryString(
            domain, interviewType, minExp, minRating, maxPrice);
        log.debug("Cache miss — calling profile service: {}", url);

        try {
            List<Map<String, Object>> results = restTemplate.getForObject(url, List.class);
            if (results == null) results = Collections.emptyList();

            // Apply pagination
            int from = page * size;
            List<Map<String, Object>> paged = results.subList(
                Math.min(from, results.size()),
                Math.min(from + size, results.size())
            );

            // Cache result
            redis.opsForValue().set(cacheKey, objectMapper.writeValueAsString(paged), CACHE_TTL);
            return paged;
        } catch (Exception e) {
            log.error("Profile service call failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<Map<String, Object>> getInterviewerProfile(String userId) {
        String url = profileServiceUrl + "/profiles/" + userId;
        try {
            Map result = restTemplate.getForObject(url, Map.class);
            return Optional.ofNullable(result);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String buildCacheKey(Object... params) {
        return "search:" + Arrays.hashCode(params);
    }

    private String buildQueryString(String domain, String interviewType,
                                     Integer minExp, Float minRating, Integer maxPrice) {
        List<String> parts = new ArrayList<>();
        if (domain != null)       parts.add("domain=" + domain);
        if (interviewType != null) parts.add("interviewType=" + interviewType);
        if (minExp != null)        parts.add("minExperience=" + minExp);
        if (minRating != null)     parts.add("minRating=" + minRating);
        if (maxPrice != null)      parts.add("maxPrice=" + maxPrice);
        return String.join("&", parts);
    }
}
