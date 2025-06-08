package com.company.employee.Controller;

import com.company.employee.DTOs.ApiResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.Objects;

@RestControllerAdvice
public class EmployeeGlobalExceptionHandler {

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<String>> DataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.ok(new ApiResponseDTO<>("error", "Database constraint violation: " + Objects.requireNonNull(ex.getRootCause()).getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<String>> handleGenericException(Exception ex) {
        return ResponseEntity.ok(new ApiResponseDTO<>("error", "An unexpected error occurred: " + ex.getMessage(), null));
    }
}
