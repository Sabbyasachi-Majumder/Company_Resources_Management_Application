package com.company.department.security;

import com.company.department.dto.ApiResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/swagger-ui/") || path.equals("/swagger-ui.html") || path.startsWith("/v3/api-docs/") || path.equals("/api/v1/department/register") || path.equals("/api/v1/department/testConnection") || path.equals("/api/v1/department/testDataBaseConnection") || path.equals("/error") || path.startsWith("/error/") || path.equals("/favicon.ico")) {
            logger.info("Skipping authentication entry point for public path: {}", path);
            return;
        }

        logger.error("Unauthorized access: {} - Path: {}", authException.getMessage(), path);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String responseBody = mapper.writeValueAsString(new ApiResponseDTO<>("error", "Unauthorized: Authentication required [AUTH_401_NO_TOKEN]", null));
            response.getWriter().write(responseBody);
        } catch (IOException e) {
            logger.error("Error writing response: {}", e.getMessage());
            throw e;
        }
    }
}