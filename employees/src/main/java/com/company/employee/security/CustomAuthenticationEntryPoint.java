//package com.company.employee.security;
//
//import com.company.employee.dto.ApiResponseDTO;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.io.IOException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.MediaType;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException) throws ServletException, JsonProcessingException {
//        String path = request.getRequestURI();
//        // Skip for public endpoints
//        if (path.startsWith("/swagger-ui/") ||
//                path.equals("/swagger-ui.html") ||
//                path.startsWith("/v3/api-docs/") ||
//                path.equals("/api/v1/employees/authenticate") ||
//                path.equals("/api/v1/employees/register") ||
//                path.equals("/api/v1/employees/testConnection") ||
//                path.equals("/api/v1/employees/testDataBaseConnection") ||
//                path.equals("/error") ||
//                path.startsWith("/error/") ||
//                path.equals("/favicon.ico")) {
//            logger.debug("Skipping authentication entry point for public path: {}", path);
//            return;
//        }
//
//        logger.error("Unauthorized access: {} - Path: {}", authException.getMessage(), path);
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            String responseBody = mapper.writeValueAsString(
//                    new ApiResponseDTO<>("error", "Unauthorized: Authentication required [AUTH_401_NO_TOKEN]", null)
//            );
//            response.getWriter().write(responseBody);
//        } catch (IOException | JsonProcessingException e) {
//            logger.error("Error writing response: {}", e.getMessage());
//            throw e;
//        } catch (java.io.IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}