package com.company.employee.controllers;

import com.company.employee.dto.*;
import com.company.employee.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//    @Tag(name = "Health Checks")
//    @Operation(summary = "Test connection to the application", description = "Tests if the connection between the client (e.g., Postman) and the Employee application is established.")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Connection established successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
    public ResponseEntity<ApiResponseDTO<String>> testPostmanToApplicationConnection() {
        loggingStart();
        logger.debug("Testing EmployeeController to Postman connection.");
        return ResponseEntity.ok(new ApiResponseDTO<>("Connection to Employee Application is successfully established.", null));
    }

    // testing Database connection
    @GetMapping(value = "/testDataBaseConnection")
//    @Tag(name = "Health Checks")
//    @Operation(summary = "Test database connection", description = "Tests if the connection to the employee database is established.")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Database connection test result", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
    public ResponseEntity<ApiResponseDTO<String>> testDataBaseConnection() {
        loggingStart();
        logger.debug("Testing EmployeeController to employee database connection.");
        try {
            return ResponseEntity.ok(new ApiResponseDTO<>(employeeService.testDatabaseConnection(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseDTO<>("Connection to database not found", null));
        }
    }

    // Displaying singular Employee Data
//    @GetMapping(name = "/{employeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})

    /// /    @Tag(name = "Employee Management")
    /// /    @Operation(summary = "Search employee by ID", description = "Searches for an employee by their ID. Requires USER or ADMIN role.")
    /// /    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employee found successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
//    public ResponseEntity<ApiResponseDTO<EmployeeFetchOrCreateDTO>> searchByEmployeeId(@PathVariable("employeeId") Long employeeId) {
//        loggingStart();
//        logger.debug("Searching employeeId {} ", employeeId);
//        return ResponseEntity.ok(new ApiResponseDTO<>(employeeService.(employeeId)));
//    }

    // Displaying Paginated Employees Data
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
//    @Tag(name = "Employee Management")
//    @Operation(summary = "Fetch all employees", description = "Retrieves all employee records from the database. Requires USER or ADMIN role.")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees fetched successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
    public ResponseEntity<ApiResponseDTO<Page<EmployeeFetchOrCreateDTO>>> fetchEmployees(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        loggingStart();
        logger.debug("Displaying all employees with page: {}, size: {}", page, size);
        return ResponseEntity.ok(new ApiResponseDTO<>(employeeService.fetchPagedDataList(page, size), null));
    }

    //Batch Adding Employees Data
//    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
//    @Tag(name = "Employee Management")
//    @Operation(summary = "Add new employees", description = "Adds a list of employees to the database. Requires ADMIN role.")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed or duplicate employee ID", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
//    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> addEmployees(@Valid @RequestBody List<EmployeeFetchOrCreateDTO> employeeFetchOrCreateRequests) {
//        loggingStart();
//        try {
//            logger.debug("Adding all records.");
//            return ResponseEntity.ok(employeeService.addDataToDataBase(employeeFetchOrCreateRequests));
//        } catch (RuntimeException e) {
//            logger.error("Adding records failed . Reason {}", e.getMessage());
//            return ResponseEntity.ok(new ApiResponseDTO<>("error", e.getMessage(), null));
//        }
//    }


    // Batch Updating Employees Data
//    @PatchMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
//    @Tag(name = "Employee Management")
//    @Operation(summary = "Update employee details", description = "Updates a list of employees in the database. Requires ADMIN role.")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
//    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> updateEmployees(@Valid @RequestBody List<BulkUpdateRequest> bulkUpdateRequestList) {
//        loggingStart();
//        logger.debug("Updating records begins");
//        try {
//            return ResponseEntity.ok(employeeService.updateDataToDataBase(bulkUpdateRequestList));
//        } catch (Exception e) {
//            logger.error("Updating records failed . Reason : {}", e.getMessage());
//            throw e;
//        }
//    }

    // Batch Delete Employee Data
//    @PostMapping(value = "/batch-delete", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
//    @Tag(name = "Employee Management")
//    @Operation(summary = "Delete employees", description = "Deletes a list of employees from the database by their IDs. Requires ADMIN role.")
//    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Employees deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class))), @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
//    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> deleteEmployees(@RequestBody BulkDeleteRequest bulkDeleteRequestList) {
//        loggingStart();
//        logger.debug("Deleting record begins");
//        try {
//            return ResponseEntity.ok(employeeService.deleteDataFromDataBase(bulkDeleteRequestList.getEmployeeIds()));
//        } catch (Exception e) {
//            logger.error("Deleting records failed . Reason : {} ", e.getMessage());
//            throw e;
//        }
//    }
}
