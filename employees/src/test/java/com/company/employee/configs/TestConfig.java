package com.company.employee.configs;

import com.company.employee.security.JwtUtil;
import com.company.employee.service.EmployeeService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;

/**
 * Test configuration to provide a mocked EmployeeService bean.
 * Used to satisfy constructor injection in EmployeeOperationsController.
 */
@Configuration
public class TestConfig {

    @Bean
    @Primary
    public EmployeeService employeeService() {
        return Mockito.mock(EmployeeService.class);
    }

    @Bean
    @Primary
    public JwtUtil jwtUtil() {
        return Mockito.mock(JwtUtil.class);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}