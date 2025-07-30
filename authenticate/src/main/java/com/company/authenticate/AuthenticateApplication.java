package com.company.authenticate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableDiscoveryClient
public class AuthenticateApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticateApplication.class, args);
    }

}
