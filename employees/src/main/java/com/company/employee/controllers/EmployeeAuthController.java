package com.company.employee.controllers;

import com.company.employee.dto.*;
import com.company.employee.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class EmployeeAuthController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeAuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public EmployeeAuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and generate JWT tokens",
            description = "Authenticates a user with username and password, returning an access token and refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, tokens generated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
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
    @Operation(summary = "Refresh JWT access token",
               description = "Generates a new access token using a valid refresh token.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid or expired refresh token",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = ApiResponseDTO.class)))
    })
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