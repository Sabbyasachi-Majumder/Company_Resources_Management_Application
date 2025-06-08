package com.company.employee.Controller;

import com.company.employee.Configs.EmployeeSecurityConfig;
import com.company.employee.DTOs.ApiResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.Objects;

@RestControllerAdvice
public class EmployeeGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeGlobalExceptionHandler.class);

    private EmployeeGlobalExceptionHandler() {
        logger.info("GlobalExceptionHandler initialized");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        return ResponseEntity.ok(new ApiResponseDTO<>("error", errorMessage.toString(), null));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.ok(new ApiResponseDTO<>("error", "Resource not found: " + ex.getMessage(), null));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        String message;
        if (ex instanceof BadCredentialsException) {
            message = "Unauthorized: Invalid username or password [AUTH_401_INVALID_CREDENTIALS]";
        } else if (ex instanceof AuthenticationCredentialsNotFoundException) {
            message = "Unauthorized: Authentication required [AUTH_401_NO_CREDENTIALS]";
        } else {
            message = "Unauthorized: Authentication failed [AUTH_401_GENERIC]";
        }
        logger.error("401 Unauthorized: {} - Path: {}", message, request.getRequestURI());
        return ResponseEntity.ok(new ApiResponseDTO<>("error", message, null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
        String message = "Forbidden: Insufficient permissions [AUTH_403_INSUFFICIENT_PERMISSIONS]";
        logger.error("403 Forbidden: {} - Path: {} - User: {}", message, request.getRequestURI(), username);
        ApiResponseDTO<String> response = new ApiResponseDTO<>("error", message, null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<String>> DataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.ok(new ApiResponseDTO<>("error", "Database constraint violation: " + Objects.requireNonNull(ex.getRootCause()).getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<String>> handleGenericException(Exception ex) {
        return ResponseEntity.ok(new ApiResponseDTO<>("error", "An unexpected error occurred: " + ex.getMessage(), null));
    }
}
