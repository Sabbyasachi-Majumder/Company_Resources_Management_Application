package com.company.department.controllers;

import com.company.department.dto.ApiResponseDTO;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.Objects;

@RestControllerAdvice
public class DepartmentGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentGlobalExceptionHandler.class);

    DepartmentGlobalExceptionHandler() {
        logger.info("GlobalExceptionHandler initialized");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        logger.error("Validation error: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDTO<>("error", errorMessage.toString(), null));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleNoSuchElementException(NoSuchElementException ex) {
        logger.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>("error", "Resource not found: " + ex.getMessage(), null));
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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseDTO<>("error", message, null));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleJwtException(JwtException ex, HttpServletRequest request) {
        String message = "Unauthorized: Invalid or expired JWT token [AUTH_401_INVALID_TOKEN]";
        logger.error("401 Unauthorized: {} - Path: {}", message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseDTO<>("error", message, null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
        String message = "Forbidden: Insufficient permissions [AUTH_403_INSUFFICIENT_PERMISSIONS]";
        logger.error("403 Forbidden: {} - Path: {} - User: {}", message, request.getRequestURI(), username);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponseDTO<>("error", message, null));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "Database constraint violation: " + Objects.requireNonNull(ex.getRootCause()).getMessage();
        logger.error("Database error: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDTO<>("error", message, null));
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleServletException(ServletException ex, HttpServletRequest request) {
        logger.error("Servlet error: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        if (ex.getCause() instanceof AuthenticationException) {
            return handleAuthenticationException((AuthenticationException) ex.getCause(), request);
        } else if (ex.getCause() instanceof AccessDeniedException) {
            return handleAccessDeniedException((AccessDeniedException) ex.getCause(), request);
        } else if (ex.getCause() instanceof JwtException) {
            return handleJwtException((JwtException) ex.getCause(), request);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO<>("error", "Internal server error: " + ex.getMessage(), null));
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiResponseDTO<String>> handleTransactionSystemException(TransactionSystemException ex, HttpServletRequest request) {
        String message = "Transaction error: " + ex.getMessage();
        logger.error("Transaction error: {} - Path: {}", message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponseDTO<>("error", message, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<String>> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error: {} - Path: {} - StackTrace: ", ex.getMessage(), request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponseDTO<>("error", "Internal server error: " + ex.getMessage(), null));
    }
}
