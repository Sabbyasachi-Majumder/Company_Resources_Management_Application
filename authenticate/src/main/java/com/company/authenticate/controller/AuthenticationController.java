package com.company.authenticate.controller;

import com.company.authenticate.dto.ApiResponseDTO;
import com.company.authenticate.dto.AuthRequestDTO;
import com.company.authenticate.dto.AuthResponseDTO;
import com.company.authenticate.security.JwtUtil;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RequestMapping("/api/v1/authenticates")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // Authenticates a user with username and password, returning an access token and refresh token
    @PostMapping("authenticate")
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
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> authenticate(@RequestBody AuthRequestDTO authRequest) {
        logger.info("Authentication attempt for user: {}", authRequest.getUsername());
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
            throw e;
        }
    }
}