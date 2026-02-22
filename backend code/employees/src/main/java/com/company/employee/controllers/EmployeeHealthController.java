package com.company.employee.controllers;

import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.service.EmployeeHealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeHealthController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeHealthController.class);

    private final EmployeeHealthService employeeHealthService;

    // Constructor injection
    public EmployeeHealthController(EmployeeHealthService employeeHealthService) {
        this.employeeHealthService = employeeHealthService;
    }

    public void loggingStart() {
        logger.info("\n\n\t\t********************* New Request Started ********************\n\n");
    }

    // testing connection
    @GetMapping(value = "/testConnection")
    @Tag(name = "Health Checks")
    @Operation(summary = "Test connection to the application", description = "Tests if the connection between the client (e.g., Postman) and the Employee application is established.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Connection established successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
    public ResponseEntity<ApiResponseDTO<String>> testPostmanToApplicationConnection() {
        loggingStart();
        logger.debug("Testing EmployeeController to Postman connection.");
        return ResponseEntity.ok(new ApiResponseDTO<>("Connection to Employee Application is successfully established."));
    }

    // testing Database connection
    @GetMapping(value = "/testDataBaseConnection")
    @Tag(name = "Health Checks")
    @Operation(summary = "Test database connection", description = "Tests if the connection to the employee database is established.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Database connection test result", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
    public ResponseEntity<ApiResponseDTO<String>> testDataBaseConnection() {
        loggingStart();
        logger.debug("Testing EmployeeController to employee database connection.");
        try {
            return ResponseEntity.ok(new ApiResponseDTO<>(employeeHealthService.testDatabaseConnection()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseDTO<>("Connection to database not found"));
        }
    }
}
