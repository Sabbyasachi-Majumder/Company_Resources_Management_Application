package com.java.employee;

import com.java.employee.beans.EmployeeDetailBean;
import com.java.employee.beans.EmployeeResponseBean;
import com.java.employee.service.EmployeeServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"beans", "repository", "service"})
public class EmployeeConfig {
    @Bean
    public EmployeeDetailBean employeeDetailBean() {
        return new EmployeeDetailBean();
    }

    @Bean
    public EmployeeResponseBean employeeResponseBean() {
        return new EmployeeResponseBean();
    }

    @Bean
    public EmployeeServiceImpl employeeService() {
        return new EmployeeServiceImpl();
    }
}
