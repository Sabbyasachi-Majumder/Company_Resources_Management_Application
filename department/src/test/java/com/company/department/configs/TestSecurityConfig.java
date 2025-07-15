package com.company.department.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Test-specific security configuration to disable JWT authentication and allow @WithMockUser.
 * Ensures ApplicationContext loads without requiring JWT-related beans.
 */
@Configuration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for tests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/department/testConnection", "/api/v1/department/testDataBaseConnection").permitAll()
                        .requestMatchers("/api/v1/department/fetchDepartment", "/api/v1/department/searchEmployee/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/department/addDepartment", "/api/v1/department/updateDepartment", "/api/v1/department/deleteDepartment").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}