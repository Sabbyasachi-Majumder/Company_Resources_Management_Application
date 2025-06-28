package com.company.authenticate.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"dto", "repository", "service"})
public class AuthenticateConfig {
}
