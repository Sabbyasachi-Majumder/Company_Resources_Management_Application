package com.company.project.config;

import com.company.project.service.ProjectServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"beans", "repository", "service"})
public class ProjectConfig {

    @Bean
    public ProjectServiceImpl projectService() {
        return new ProjectServiceImpl();
    }
}
