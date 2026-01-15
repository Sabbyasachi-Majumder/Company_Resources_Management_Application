package com.company.employee.controllers;

import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.dto.ErrorDetailsDto;
import com.company.employee.dto.OperationSummaryDTO;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
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

import java.util.*;

@RestControllerAdvice
public class EmployeeGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeGlobalExceptionHandler.class);

    EmployeeGlobalExceptionHandler() {
        logger.info("GlobalExceptionHandler initialized");
    }

    // Overall Failure – these will typically trigger -1 in operationDetails (full failure, no changes applied)
    @ExceptionHandler(ServletException.class)
    @Schema(description = "Servlet-level errors (e.g., unexpected filter chain issues or wrapped authentication/authorization problems). Usually indicates overall operation failure (-1 marker).")
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
                .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto("", "Internal server error: " + ex.getMessage())));
    }

    @ExceptionHandler(JwtException.class)
    @Schema(description = "JWT token is invalid, expired, malformed, or signature verification failed. Results in overall operation failure (-1 marker).")
    public ResponseEntity<ApiResponseDTO<String>> handleJwtException(JwtException ex, HttpServletRequest request) {
        String message = "Unauthorized: Invalid or expired JWT token";
        logger.error("401 Unauthorized: {} - Path: {}", message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto("INVALID_TOKEN", message)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Schema(description = "Request body validation failed (e.g., missing required fields, invalid format). Entire operation is rejected (-1 marker).")
    public ResponseEntity<ApiResponseDTO<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        logger.error("Validation error: {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto("BAD_REQUEST", errorMessage.toString())));
    }

    @ExceptionHandler(AuthenticationException.class)
    @Schema(description = "Authentication failed (invalid credentials, missing credentials, or other auth issues). Entire operation is rejected (-1 marker).")
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
                .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto(null, message)));
    }

    @ExceptionHandler(AccessDeniedException.class)
    @Schema(description = "User is authenticated but lacks required permissions/roles. Entire operation is rejected (-1 marker).")
    public ResponseEntity<ApiResponseDTO<String>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";
        String message = "Forbidden: Insufficient permissions [AUTH_403_INSUFFICIENT_PERMISSIONS]";
        logger.error("403 Forbidden: {} - Path: {} - User: {}", message, request.getRequestURI(), username);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto(null, message)));
    }

    @ExceptionHandler(TransactionSystemException.class)
    @Schema(description = "Transaction-level failure (e.g., commit/rollback issue, connection problem during transaction). Entire operation is rejected (-1 marker).")
    public ResponseEntity<ApiResponseDTO<String>> handleTransactionSystemException(TransactionSystemException ex, HttpServletRequest request) {
        String message = "Transaction error: " + ex.getMessage();
        logger.error("Transaction error: {} - Path: {}", message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto(null, message)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @Schema(description = "Invalid request parameters (e.g., invalid pagination values). Entire operation is rejected (-1 marker).")
    public ResponseEntity<ApiResponseDTO<String>> handleIllegalArgumentException(IllegalArgumentException ex, Pageable pageable, HttpServletRequest request) {
        logger.error("IllegalArgumentException : {} - Path: {} - StackTrace: ", ex.getMessage(), request.getRequestURI(), ex);
        if (pageable.getPageNumber() <= 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto(null, "Total number of records is lower than 1.")));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto("BAD_REQUEST", "Current page " + (pageable.getPageNumber()) + " is bigger than total number of pages available.")));
    }

    @ExceptionHandler(Exception.class)
    @Schema(description = "Catch-all for unexpected/unhandled server-side errors. Treated as overall operation failure (-1 marker).")
    public ResponseEntity<ApiResponseDTO<String>> handleGenericException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error: {} - Path: {} - StackTrace: ", ex.getMessage(), request.getRequestURI(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto(null, "Generic Internal server error: " + ex.getMessage())));
    }

    // Per Item Failure – these can be mapped to positive employeeId keys in operationDetails
    @ExceptionHandler(NoSuchElementException.class)
    @Schema(description = "Requested resource/employee not found. In bulk operations, this is a per-item failure (positive employeeId key).")
    public ResponseEntity<ApiResponseDTO<String>> handleNoSuchElementException(NoSuchElementException ex) {
        logger.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto(null, "Resource not found: " + ex.getMessage())));
    }

    // Per Item Failure – these can be mapped to positive employeeId keys in operationDetails
    @ExceptionHandler(EntityNotFoundException.class)
    @Schema(description = "Requested resource/employee not found. In bulk operations, this is a per-item failure (positive employeeId key).")
    public ResponseEntity<ApiResponseDTO<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto(null, "Resource not found: " + ex.getMessage())));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @Schema(description = "Database constraint violation (e.g., duplicate unique value, foreign key violation). In bulk operations, this is usually a per-item failure (positive employeeId key).")
    public ResponseEntity<ApiResponseDTO<String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "Database constraint violation: " + Objects.requireNonNull(ex.getRootCause()).getMessage();
        logger.error("Database error: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponseDTO<>(null, null, new ErrorDetailsDto(null, message)));
    }

    // Operation Succeeded (Full or Partial) but Fetching/Refreshing failed – trigger -2 warning
    // Note: These are NOT caught in global handler — they are caught locally in the mutation methods
}
