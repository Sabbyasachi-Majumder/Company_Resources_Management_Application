/*Extracts JWT from Authorization: Bearer <token> header.
Validates token and sets authentication in SecurityContext. Runs once per request.*/

package com.company.employee.security;

import com.company.employee.service.CustomUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private final JwtUtil jwtUtil;
    private final CustomUserServiceImpl userDetailsService;

    public JwtRequestFilter(JwtUtil jwtUtil, CustomUserServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        logger.debug("Processing request in JwtRequestFilter - Path: {}", path);

        // Check if JWT processing should be skipped (API and web public endpoints)
        if (shouldSkipJwtProcessing(path)) {
            logger.debug("Skipping JWT filter for public endpoint: {}", path);
            chain.doFilter(request, response);
            return;
        }

        // API-specific JWT authentication logic
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtil.extractUsername(jwt);
            } else {
                logger.debug("No JWT token provided in request - Path: {}", path);
                chain.doFilter(request, response); // Proceed without authentication for non-API paths
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("JWT authentication successful for user: {}", username);
                } else {
                    logger.warn("Invalid JWT token for user: {}", username);
                    throw new AuthenticationException("Invalid JWT token") {
                    };
                }
            }
            chain.doFilter(request, response);
        } catch (AuthenticationException | io.jsonwebtoken.JwtException e) {
            logger.error("Authentication error: {} - Path: {}", e.getMessage(), path);
            throw e; // Propagate to Spring Security's ExceptionTranslationFilter for API requests
        } catch (Exception e) {
            logger.error("Unexpected error in JWT filter: {} - Path: {}", e.getMessage(), path);
            throw new ServletException("Unexpected error during JWT processing", e);
        }
    }

    // Shared method for skipping JWT processing (API, web public endpoints, and static resources)
    private boolean shouldSkipJwtProcessing(String path) {
        // Normalize path for consistent matching
        String normalizedPath = path.toLowerCase().trim();
        if (normalizedPath.endsWith("/")) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
        }

        // Skip paths for API, web public endpoints, and static resources
        boolean shouldSkip =
                // Web-specific public endpoints
                normalizedPath.startsWith("/web/") ||
                        normalizedPath.equals("/login") ||
                        // API-specific public endpoints
                        normalizedPath.startsWith("/swagger-ui") ||
                        normalizedPath.equals("/swagger-ui.html") ||
                        normalizedPath.startsWith("/v3/api-docs") ||
                        normalizedPath.equals("/favicon.ico") ||
                        normalizedPath.startsWith("/error") ||
                        normalizedPath.equals("/api/v1/employees/authenticate") ||
                        normalizedPath.equals("/api/v1/employees/register") ||
                        normalizedPath.equals("/api/v1/employees/testconnection") ||
                        normalizedPath.equals("/api/v1/employees/testdatabaseconnection") ||
                        // Static resources
                        normalizedPath.startsWith("/css/") ||
                        normalizedPath.startsWith("/js/");
        if (shouldSkip) {
            logger.debug("Path {} is configured to skip JWT processing", path);
        } else {
            logger.debug("Path {} requires JWT processing", path);
        }
        return shouldSkip;
    }
}
