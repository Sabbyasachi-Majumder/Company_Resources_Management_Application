package com.company.employee.configs;

import com.company.employee.repository.EmployeeRepository;
import com.company.employee.service.EmployeeServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"dto", "repository", "service"})
public class EmployeeConfig {

    EmployeeRepository employeeRepository;
    DataSource dataSource;

    @Bean
    public EmployeeServiceImpl employeeService() {
        return new EmployeeServiceImpl(employeeRepository, dataSource);
    }
}
