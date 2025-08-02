package com.company.authenticate.controller;

import com.company.authenticate.dto.ApiResponseDTO;
import com.company.authenticate.dto.AuthRequestDTO;
import com.company.authenticate.dto.AuthResponseDTO;
import com.company.authenticate.service.AuthenticateServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RequestMapping("/api/v1/authenticates")
@Validated
public class AuthenticationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticateServiceImpl authenticateUser;

    public AuthenticationController(AuthenticateServiceImpl authenticateUser) {
        this.authenticateUser = authenticateUser;
    }

    public void loggingStart() {
        logger.debug("\n\n\t\t********************* New Request Started ********************\n\n");
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
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> authenticate(@Valid @RequestBody AuthRequestDTO authRequest) {
        loggingStart();
        logger.info("Authentication attempt for user: {}", authRequest.getUserName());
        try {
            logger.info("JWT generated for user: {}", authRequest.getUserName());
            return ResponseEntity.ok((authenticateUser.authenticateUser(authRequest.getUserName(), authRequest.getPassword())));
        } catch (AuthenticationException e) {
            logger.error("Login failed for user: {} - {}", authRequest.getUserName(), e.getMessage());
            throw e;
        }
    }
}