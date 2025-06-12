package com.company.employee.configs;

import com.company.employee.service.EmployeeServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"dto", "repository", "service"})
public class EmployeeConfig {

    @Bean
    public EmployeeServiceImpl employeeService() {
        return new EmployeeServiceImpl();
    }
}
