package com.company.api_gateway.security;

import com.company.api_gateway.dto.ApiResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        String path = exchange.getRequest().getURI().getPath();
        // Skip for public endpoints
        if (path.startsWith("/swagger-ui/") ||
                path.equals("/swagger-ui.html") ||
                path.startsWith("/v3/api-docs/") ||
                path.equals("/login") ||
                path.equals("/api/v1/employees/authenticate") ||
                path.equals("/api/v1/employees/register") ||
                path.equals("/api/v1/employees/testConnection") ||
                path.equals("/api/v1/employees/testDataBaseConnection")) {
            logger.debug("Skipping authentication entry point for public path: {}", path);
            return Mono.empty();
        }

        logger.error("Unauthorized access: {} - Path: {}", ex.getMessage(), path);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String responseBody = mapper.writeValueAsString(
                    new ApiResponseDTO<String>("error", "Unauthorized: Authentication required [AUTH_401_NO_TOKEN]",
                            null)
            );
            byte[] bytes = responseBody.getBytes();
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            logger.error("Error writing response: {}", e.getMessage());
            return Mono.error(e);
        }
    }
}