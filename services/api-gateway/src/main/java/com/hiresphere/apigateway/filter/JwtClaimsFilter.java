package com.hiresphere.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Extracts userId, role, and email from the validated Cognito JWT
 * and forwards them as X-User-* headers to all downstream microservices.
 * Services trust these headers without re-validating the token.
 */
@Component
public class JwtClaimsFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
            .map(ctx -> ctx.getAuthentication())
            .filter(auth -> auth instanceof JwtAuthenticationToken)
            .cast(JwtAuthenticationToken.class)
            .map(JwtAuthenticationToken::getToken)
            .flatMap(jwt -> {
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id",    jwt.getSubject())
                    .header("X-User-Role",  extractRole(jwt))
                    .header("X-User-Email", jwt.getClaimAsString("email"))
                    .build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            })
            .switchIfEmpty(chain.filter(exchange));
    }

    private String extractRole(Jwt jwt) {
        var groups = jwt.getClaimAsStringList("cognito:groups");
        if (groups != null && groups.contains("INTERVIEWER")) return "INTERVIEWER";
        return "CANDIDATE";
    }

    @Override
    public int getOrder() { return -1; }
}
