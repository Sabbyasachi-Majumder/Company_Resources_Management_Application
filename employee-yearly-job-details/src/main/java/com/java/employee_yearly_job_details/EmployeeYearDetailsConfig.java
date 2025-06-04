package com.java.employee_yearly_job_details;

import com.java.employee_yearly_job_details.beans.EmployeeYrJobDetailsDetailBean;
import com.java.employee_yearly_job_details.beans.EmployeeYrJobDetailsResponseBean;
import com.java.employee_yearly_job_details.service.EmployeeYrJobDetailsServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"beans", "repository", "service"})
public class EmployeeYearDetailsConfig {
    @Bean
    public EmployeeYrJobDetailsDetailBean employeeDetailBean() {
        return new EmployeeYrJobDetailsDetailBean();
    }

    @Bean
    public EmployeeYrJobDetailsResponseBean employeeResponseBean() {
        return new EmployeeYrJobDetailsResponseBean();
    }

    @Bean
    public EmployeeYrJobDetailsServiceImpl employeeService() {
        return new EmployeeYrJobDetailsServiceImpl();
    }
}
