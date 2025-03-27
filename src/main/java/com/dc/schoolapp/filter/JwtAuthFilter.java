package com.dc.schoolapp.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtAuthFilter implements GatewayFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Lazy
    @Autowired
    private UserDetailsService userDetailsService;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        logger.info("🔍 JwtAuthFilter invoked for request: " + request.getURI());

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            logger.error("🚨 Missing Authorization Header");
            return chain.filter(exchange);
        }

        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token == null || !token.startsWith("Bearer ")) {
            logger.error("🚨 Invalid Authorization Header: " + token);
            return chain.filter(exchange);
        }

        logger.info("✅ JWT Token received: " + token);

        ServerHttpRequest modifiedRequest = exchange.getRequest()
                .mutate()
                .header(HttpHeaders.AUTHORIZATION, token)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            try {
//                Claims claims = Jwts.parserBuilder()
//                        .setSigningKey(getSigningKey())
//                        .build()
//                        .parseClaimsJws(token)
//                        .getBody();
//                logger.info("JWT Token Validated: {}", claims.getSubject());
//
//                // Forward the user information
//                exchange.getRequest().mutate().header("user", claims.getSubject()).build();
//            } catch (Exception e) {
//                logger.error("JWT Validation Failed: {}", e.getMessage());
//
//                return exchange.getResponse().setComplete(); // Block request if token is invalid
//            }
//        }
//        return chain.filter(exchange);
//    }


}
