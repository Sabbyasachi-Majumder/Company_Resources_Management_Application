package com.company.project.config;

import com.company.project.repository.ProjectRepository;
import com.company.project.service.ProjectServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"beans", "repository", "service"})
public class ProjectConfig {

    ProjectRepository projectRepository;
    DataSource dataSource;

    @Bean
    public ProjectServiceImpl projectService() {
        return new ProjectServiceImpl(projectRepository,dataSource);
    }
}
