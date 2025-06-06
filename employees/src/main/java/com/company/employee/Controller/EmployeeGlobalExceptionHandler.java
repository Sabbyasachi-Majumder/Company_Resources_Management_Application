package com.company.employee.Controller;

import com.company.employee.DTOs.EmployeeResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class EmployeeGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EmployeeResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }
        erBean.setResponseMessage(errorMessage.toString());
        erBean.setResponseStatusCode(HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erBean);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<EmployeeResponseDTO> handleNoSuchElementException(NoSuchElementException ex) {
        EmployeeResponseDTO response = new EmployeeResponseDTO();
        response.setResponseMessage("Resource not found: " + ex.getMessage());
        response.setResponseStatusCode(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<EmployeeResponseDTO> handleGenericException(Exception ex) {
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        erBean.setResponseMessage("An unexpected error occurred: " + ex.getMessage());
        erBean.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erBean);
    }
}
