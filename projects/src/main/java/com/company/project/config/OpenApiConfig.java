package com.company.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiConfig.class);

    @Bean
    public OpenAPI customOpenAPI() {
        logger.info("Configuring OpenAPI documentation with JWT authorization");
        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8081").description("Local Development Server"))
                .addServersItem(new Server().url("/project").description("API Gateway Route for Microservices"))
                .info(new Info()
                        .title("Employee Service API")
                        .description("API for managing project data in a microservices architecture with JWT-based authentication")
                        .version("1.0.0")
                        .contact(new Contact().name("Sabbyasachi Majumder").email("sabbyasachi.majumder.offcial@gmail.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token obtained from /login. Use 'Bearer <token>' in Authorization header.")))
                .addTagsItem(new Tag().name("Actuator").description("Spring Boot Actuator endpoints for monitoring"))
                .addTagsItem(new Tag().name("Health Checks").description("Endpoints for testing connections"))
                .addTagsItem(new Tag().name("Employee Management").description("Endpoints for managing project data"));
    }
}
