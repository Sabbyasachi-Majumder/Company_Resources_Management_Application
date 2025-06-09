package com.company.employee.Controller;

import com.company.employee.DTOs.*;
import com.company.employee.Security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeAuthController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeAuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public EmployeeAuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(@RequestBody AuthRequestDTO authRequest) {
        logger.info("Login attempt for user: {}", authRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            logger.info("JWT generated for user: {}", authRequest.getUsername());
            return ResponseEntity.ok(new ApiResponseDTO<>("success",
                    "Login successful [AUTH_200_OK]", new AuthResponseDTO(accessToken, refreshToken)));
        } catch (AuthenticationException e) {
            logger.error("Login failed for user: {} - {}", authRequest.getUsername(), e.getMessage());
            throw e; // Handled by GlobalExceptionHandler
        }
    }

    /*@PostMapping("/refresh")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> refreshToken(@RequestBody RefreshRequestDTO refreshRequest) {
        logger.info("Refresh token attempt for user");
        try {
            String username = jwtUtil.extractUsername(refreshRequest.getRefreshToken());
            UserDetails userDetails = (UserDetails) authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, null)).getPrincipal();
            if (jwtUtil.isTokenValid(refreshRequest.getRefreshToken(), userDetails)) {
                String newAccessToken = jwtUtil.generateToken(userDetails);
                logger.info("New access token generated for user: {}", username);
                return ResponseEntity.ok(new ApiResponseDTO<AuthResponseDTO>("success",
                        "Token refreshed [AUTH_200_OK]", new AuthResponseDTO(newAccessToken, refreshRequest.getRefreshToken())));
            }
            throw new RuntimeException("Invalid refresh token");
        } catch (Exception e) {
            logger.error("Refresh token failed: {}", e.getMessage());
            throw new RuntimeException("Invalid refresh token [AUTH_401_INVALID_REFRESH]");
        }
    }*/
}