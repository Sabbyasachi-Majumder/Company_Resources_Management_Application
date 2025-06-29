package com.company.employee.configs;

import com.company.employee.security.JwtUtil;
import com.company.employee.service.EmployeeService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
}