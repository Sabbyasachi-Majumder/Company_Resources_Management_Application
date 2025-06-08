package com.company.employee.Controller;

import com.company.employee.DTOs.ApiResponseDTO;
import com.company.employee.DTOs.AuthRequestDTO;
import com.company.employee.DTOs.AuthResponseDTO;
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
            String jwt = jwtUtil.generateToken(userDetails);
            logger.info("JWT generated for user: {}", authRequest.getUsername());
            return ResponseEntity.ok(new ApiResponseDTO<AuthResponseDTO>("success", "Login successful [AUTH_200_OK]", new AuthResponseDTO(jwt)));
        } catch (AuthenticationException e) {
            logger.error("Login failed for user: {} - {}", authRequest.getUsername(), e.getMessage());
            throw e; // Handled by GlobalExceptionHandler
        }
    }
}