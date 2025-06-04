package com.java.department;

import com.java.department.beans.DepartmentBean;
import com.java.department.beans.DepartmentResponseBean;
import com.java.department.service.DepartmentServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"beans", "repository", "service"})
public class DepartmentConfig {
    @Bean
    public DepartmentBean departmentBean(){
        return new DepartmentBean();
    }

    @Bean
    public DepartmentResponseBean departmentResponseBean(){
        return new DepartmentResponseBean();
    }

    @Bean
    public DepartmentServiceImpl departmentService(){
        return new DepartmentServiceImpl();
    }
}
