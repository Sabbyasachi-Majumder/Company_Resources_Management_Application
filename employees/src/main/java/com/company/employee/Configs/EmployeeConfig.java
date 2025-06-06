package com.company.employee.Configs;

import com.company.employee.DTOs.EmployeeRequestDTO;
import com.company.employee.DTOs.EmployeeResponseDTO;
import com.company.employee.Services.EmployeeServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"DTOs", "Repositories", "Services"})
public class EmployeeConfig {
    @Bean
    public EmployeeRequestDTO employeeDetailBean() {
        return new EmployeeRequestDTO();
    }

    @Bean
    public EmployeeResponseDTO employeeResponseBean() {
        return new EmployeeResponseDTO();
    }

    @Bean
    public EmployeeServiceImpl employeeService() {
        return new EmployeeServiceImpl();
    }
}
