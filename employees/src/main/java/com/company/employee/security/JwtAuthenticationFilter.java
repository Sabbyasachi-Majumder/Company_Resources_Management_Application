//package com.company.employee.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class JwtAuthenticationFilter implements GlobalFilter {
//    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        logger.info("\n\n\t\t********************* New Request Started ********************\n\n");
//        String path = exchange.getRequest().getURI().getPath();
//        logger.debug("Processing request in JwtAuthenticationFilter - Path: {}", path);
//
//        // Skip JWT validation for public endpoints
//        if (shouldSkipJwtProcessing(path)) {
//            logger.debug("Skipping JWT validation for public endpoint: {}", path);
//            return chain.filter(exchange);
//        }
//
//        // Extract and validate JWT
//        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            logger.error("No JWT token provided - Path: {}", path);
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        String token = authHeader.substring(7);
//        try {
//            Claims claims = Jwts.parser()
//                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                    .build()
//                    .parseSignedClaims(token)
//                    .getPayload();
//            logger.debug("JWT validated for user: {}", claims.getSubject());
//            return chain.filter(exchange);
//        } catch (Exception e) {
//            logger.error("Invalid JWT token: {} - Path: {}", e.getMessage(), path);
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//    }
//
//    private boolean shouldSkipJwtProcessing(String path) {
//        String normalizedPath = path.toLowerCase().trim();
//        if (normalizedPath.endsWith("/")) {
//            normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
//        }
//        boolean shouldSkip = normalizedPath.equals("/login") ||
//                normalizedPath.startsWith("/swagger-ui") ||
//                normalizedPath.equals("/swagger-ui.html") ||
//                normalizedPath.startsWith("/v3/api-docs") ||
//                normalizedPath.equals("/api/v1/employees/authenticate") ||
//                normalizedPath.equals("/api/v1/employees/register") ||
//                normalizedPath.equals("/api/v1/employees/testconnection") ||
//                normalizedPath.equals("/api/v1/employees/testdatabaseconnection");
//        if (shouldSkip) {
//            logger.debug("Path {} is configured to skip JWT processing", path);
//        } else {
//            logger.debug("Path {} requires JWT processing", path);
//        }
//        return shouldSkip;
//    }
//}