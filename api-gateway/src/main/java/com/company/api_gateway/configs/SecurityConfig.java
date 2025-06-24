package com.company.api_gateway.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/employees/authenticate", "/api/v1/employees/register",
                                "/api/v1/employees/testConnection", "/api/v1/employees/testDataBaseConnection",
                                "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .pathMatchers("/api/v1/employees/**").authenticated()
                        .pathMatchers("/actuator/**").hasRole("ADMIN")
                        .anyExchange().permitAll()
                )
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(authenticationManager());
        filter.setServerAuthenticationConverter(exchange -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
                    Claims claims =Jwts.parser()
                            .verifyWith(key)
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();
                    String username = claims.getSubject();
                    String roles = claims.get("roles", String.class);
                    logger.debug("JWT validated for user: {}", username);
                    return Mono.just(new UsernamePasswordAuthenticationToken(
                            username, null, List.of(new SimpleGrantedAuthority(roles))
                    ));
                } catch (Exception e) {
                    logger.error("JWT validation failed: {}", e.getMessage());
                    return Mono.empty();
                }
            }
            return Mono.empty();
        });
        filter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/v1/employees/**", "/actuator/**"));
        return filter;
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return authentication -> Mono.just(authentication)
                .map(auth -> {
                    auth.setAuthenticated(true);
                    return auth;
                });
    }
}