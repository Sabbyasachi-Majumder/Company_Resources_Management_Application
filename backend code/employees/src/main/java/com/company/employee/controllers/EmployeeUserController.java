package com.company.employee.controllers;

import com.company.employee.dto.*;
import com.company.employee.service.EmployeeUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Endpoints for Employee Operations with User rights")
public class EmployeeUserController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeUserController.class);
    private final EmployeeUserService employeeUserService;

    // Constructor injection
    public EmployeeUserController(EmployeeUserService employeeUserService){
        this.employeeUserService = employeeUserService;
    }

    public void loggingStart() {
        logger.info("\n\n\t\t********************* New Request Started ********************\n\n");
    }

    // Counting the total no of employee records
    @GetMapping(value = "/countTotal")
    @Tag(name = "Employee management")
    @Operation(summary = "Test database connection", description = "Tests if the connection to the employee database is established.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Database connection test result", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponseDTO.class)))})
    public ResponseEntity<ApiResponseDTO<Long>> countEmployees() {
        loggingStart();
        logger.debug("Counting the total amount of employee entries.");
        return ResponseEntity.ok(new ApiResponseDTO<>(employeeUserService.countEntities()));
    }

    @GetMapping("/metadata")
    @Tag(name = "Employee Datatable Metadata management")
    @Operation(summary = "Get column metadata for employees table")
    @Cacheable("employee-metadata")
    public ResponseEntity<ApiResponseDTO<List<ColumnMetadata>>> getColumnMetadata() {
        List<ColumnMetadata> metadata = employeeUserService.getEmployeeColumnMetadata();
        return ResponseEntity.ok(new ApiResponseDTO<>(metadata));
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
        return ResponseEntity.ok(new ApiResponseDTO<>(employeeUserService.searchDataBase(employeeId)));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Fetch all employees (paginated and sorted)", description = "Retrieves paginated and sorted list of employees, according to the criteria user provides. Requires USER or ADMIN role.")
    public ResponseEntity<ApiResponseDTO<Page<EmployeeDTO>>> fetchEmployees(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "employeeId") String sortByColumnName, @RequestParam(defaultValue = "ASC") String sortOrder) {
        loggingStart();
        logger.debug("Displaying all employees with page: {}, size: {}, column to sort: {}, sorting order:{}", page, size, sortByColumnName, sortOrder);
        return ResponseEntity.ok(new ApiResponseDTO<>(employeeUserService.fetchPagedDataList(page, size, sortByColumnName, sortOrder)));
    }
}
