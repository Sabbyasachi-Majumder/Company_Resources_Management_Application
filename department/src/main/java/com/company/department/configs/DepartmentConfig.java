package com.company.department.configs;

import com.company.department.service.DepartmentServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"dto", "repository", "service"})
public class DepartmentConfig {

    @Bean
    public DepartmentServiceImpl DepartmentService() {
        return new DepartmentServiceImpl();
    }
}
