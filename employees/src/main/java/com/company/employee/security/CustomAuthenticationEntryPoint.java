package com.company.employee.security;

import com.company.employee.dto.ApiResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        String path = request.getRequestURI();
        if (path.startsWith("/swagger-ui/") || path.equals("/swagger-ui.html") || path.startsWith("/v3/api-docs/") || path.equals("/api/v1/employees/register") || path.equals("/api/v1/employees/testConnection") || path.equals("/api/v1/employees/testDataBaseConnection")) {
            logger.debug("Skipping authentication entry point for public path: {}", path);
            return;
        }
        logger.error("Unauthorized access: {} - Path: {}", authException.getMessage(), path);
        throw authException;
    }
}