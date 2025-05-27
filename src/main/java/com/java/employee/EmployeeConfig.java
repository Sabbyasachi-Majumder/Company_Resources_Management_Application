package com.java.employee;

import com.java.employee.beans.EmployeeDetailBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@ComponentScan(basePackages = {"beans", "repository", "response"})
public class EmployeeConfig {
    @Bean
    public EmployeeDetailBean employeeDetailBean(){
        return new EmployeeDetailBean();
    }
}
