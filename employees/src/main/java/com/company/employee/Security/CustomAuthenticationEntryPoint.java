package com.company.employee.security;

import com.company.employee.dto.ApiResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        String path = request.getRequestURI();
        // Skip processing for public endpoints
        if (path.startsWith("/swagger-ui/") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/v3/api-docs/") ||
                path.equals("/favicon.ico") ||
                path.startsWith("/error") ||
                path.equals("/login") ||
                path.equals("/api/v1/employees/testConnection") ||
                path.equals("/api/v1/employees/testDataBaseConnection")) {
            logger.debug("Skipping authentication entry point for public path: {}", path);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        logger.error("Unauthorized access: {} - Path: {}", authException.getMessage(), path);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ApiResponseDTO<String> apiResponse = new ApiResponseDTO<>("error",
                "Unauthorized: Authentication required [AUTH_401_NO_TOKEN]", null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), apiResponse);
    }
}

