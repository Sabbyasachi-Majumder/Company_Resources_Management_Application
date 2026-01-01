package com.company.employee.configs;

import com.company.employee.security.CustomAuthenticationEntryPoint;
import com.company.employee.security.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class EmployeeSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeSecurityConfig.class);

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public EmployeeSecurityConfig(CustomAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Creating rules to access endpoints");
        logger.debug("JWT Filter Invoked");
        http.csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api/v1/employees/testConnection", "/api/v1/employees/testDataBaseConnection").permitAll()
                        .requestMatchers("/testConnection", "/testDataBaseConnection").permitAll()
                        .requestMatchers("/fetchEmployees", "/searchEmployee/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/addEmployees", "/updateEmployees", "/deleteEmployees").hasRole("ADMIN")
                        .requestMatchers("/actuator/**").authenticated()
                        .anyRequest().permitAll())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        logger.info("Rules to access endpoints created and enforced");
        return http.build();
    }
}
