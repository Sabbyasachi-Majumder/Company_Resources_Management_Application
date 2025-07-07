package com.company.api_gateway.configs;

import com.company.api_gateway.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtAuthenticationFilter jwtFilter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Non-deprecated CSRF disable
                .authorizeExchange(exchanges -> exchanges
                        //public endpoints , doesn't need authorization check
                        .pathMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .pathMatchers("/api/v1/authenticates/**").permitAll()
                        .pathMatchers("/api/v1/employees/testConnection", "/api/v1/employees/testDataBaseConnection").permitAll()
                        .pathMatchers("/api/v1/project/testConnection", "/api/v1/project/testDataBaseConnection").permitAll()
                        .pathMatchers("/api/v1/department/testConnection", "/api/v1/department/testDataBaseConnection").permitAll()
                        //restricted authorization endpoints
                        .pathMatchers("/api/v1/employees/**").authenticated()
                        .pathMatchers("/api/v1/project/**").authenticated()
                        .pathMatchers("/api/v1/department/**").authenticated()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}