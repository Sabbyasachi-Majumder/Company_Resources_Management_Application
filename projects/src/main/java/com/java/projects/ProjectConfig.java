package com.java.projects;

import com.java.projects.beans.ProjectDetailBean;
import com.java.projects.beans.ProjectResponseBean;
import com.java.projects.service.ProjectServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"beans", "repository", "service"})
public class ProjectConfig {
    @Bean
    public ProjectDetailBean employeeDetailBean() {
        return new ProjectDetailBean();
    }

    @Bean
    public ProjectResponseBean employeeResponseBean() {
        return new ProjectResponseBean();
    }

    @Bean
    public ProjectServiceImpl employeeService() {
        return new ProjectServiceImpl();
    }
}
