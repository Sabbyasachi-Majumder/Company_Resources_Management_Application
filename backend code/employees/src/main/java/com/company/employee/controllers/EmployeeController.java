package com.company.employee.controllers;

import com.company.employee.dto.*;
import com.company.employee.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    // Constructor injection
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
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
            return ResponseEntity.ok(new ApiResponseDTO<>(employeeService.testDatabaseConnection()));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseDTO<>("Connection to database not found"));
        }
    }

    // testing Database connection
    @GetMapping(value = "/countTotal")
    @Tag(name = "Employee management")
    @Operation(summary = "Test database connection", description = "Tests if the connection to the employee database is established.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Database connection test result", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
    public ResponseEntity<ApiResponseDTO<Long>> countEmployees() {
        loggingStart();
        logger.debug("Counting the total amount of employee entries.");
        return ResponseEntity.ok(new ApiResponseDTO<>(employeeService.countEntities()));
    }

    // Displaying singular Employee Data
    @GetMapping(value = "/{employeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Search employee by ID", description = "Retrieves a single employee by ID. Requires USER or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<ApiResponseDTO<EmployeeDTO>> searchByEmployeeId(@PathVariable("employeeId") Long employeeId) {
        loggingStart();
        logger.debug("Searching employeeId {} ", employeeId);
        return ResponseEntity.ok(new ApiResponseDTO<>(employeeService.searchDataBase(employeeId)));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Fetch all employees (paginated)", description = "Retrieves paginated list of employees. Requires USER or ADMIN role.")
    public ResponseEntity<ApiResponseDTO<Page<EmployeeDTO>>> fetchEmployees(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        loggingStart();
        logger.debug("Displaying all employees with page: {}, size: {}", page, size);
        return ResponseEntity.ok(new ApiResponseDTO<>(employeeService.fetchPagedDataList(page, size)));
    }

    //Batch Adding Employees Data
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Add new employees", description = "Adds a list of employees to the database. Requires ADMIN role.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed or duplicate employee ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
    public ResponseEntity<ApiResponseDTO<OperationSummaryDTO>> addEmployees(@Valid @RequestBody List<EmployeeDTO> employeeFetchOrCreateRequestList) {
        loggingStart();
        try {
            logger.debug("Adding all records.");
            return ResponseEntity.ok(new ApiResponseDTO<>(employeeService.addDataToDataBase(employeeFetchOrCreateRequestList)));
        } catch (RuntimeException e) {
            logger.error("Adding records failed . Reason {}", e.getMessage());
            throw e;
        }
    }


    // Batch Updating Employees Data
    @PatchMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Update employee details", description = "Updates a list of employees in the database. Requires ADMIN role.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
    public ResponseEntity<ApiResponseDTO<OperationSummaryDTO>> updateEmployees(@Valid @RequestBody List<BulkUpdateRequest> bulkUpdateRequest) {
        loggingStart();
        logger.debug("Updating records begins");
        try {
            return ResponseEntity.ok(new ApiResponseDTO<>(employeeService.bulkUpdateDataToDataBase(bulkUpdateRequest)));
        } catch (Exception e) {
            logger.error("Updating records failed . Reason : {}", e.getMessage());
            throw e;
        }
    }

    // Batch Delete Employee Data
    @PostMapping(value = "/batch-delete", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Delete employees", description = "Deletes a list of employees from the database by their IDs. Requires ADMIN role.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
    public ResponseEntity<ApiResponseDTO<OperationSummaryDTO>> bulkDeleteEmployees(@RequestBody BulkDeleteRequest bulkDeleteRequestList) {
        loggingStart();
        logger.debug("Deleting record begins");
        try {
            return ResponseEntity.ok(new ApiResponseDTO<>(employeeService.bulkDeleteDataFromDataBase(bulkDeleteRequestList.getEmployeeIds())));
        } catch (Exception e) {
            logger.error("Deleting records failed . Reason : {} ", e.getMessage());
            throw e;
        }
    }
}
