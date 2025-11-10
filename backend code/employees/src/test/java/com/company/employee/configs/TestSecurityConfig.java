package com.company.employee.configs;

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
                        .requestMatchers("/api/v1/employees/testConnection", "/api/v1/employees/testDataBaseConnection").permitAll()
                        .requestMatchers("/api/v1/employees/fetchEmployees", "/api/v1/employees/searchEmployee/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/employees/addEmployees", "/api/v1/employees/updateEmployees", "/api/v1/employees/deleteEmployees").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}