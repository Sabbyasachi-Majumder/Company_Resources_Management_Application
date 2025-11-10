package com.company.authenticate.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.company.authenticate")
@EnableJpaRepositories(basePackages = "com.company.authenticate.repository")
public class AuthenticateConfig {
}
