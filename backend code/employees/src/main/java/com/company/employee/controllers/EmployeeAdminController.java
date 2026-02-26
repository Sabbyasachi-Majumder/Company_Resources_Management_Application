package com.company.employee.controllers;

import com.company.employee.dto.*;
import com.company.employee.service.EmployeeAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Endpoints for Employee Operations with Admin rights")
public class EmployeeAdminController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeAdminController.class);
    private final EmployeeAdminService employeeAdminService;

    // Constructor injection
    public EmployeeAdminController(EmployeeAdminService employeeAdminService) {
        this.employeeAdminService = employeeAdminService;
    }

    public void loggingStart() {
        logger.info("\n\n\t\t********************* New Request Started ********************\n\n");
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
            return ResponseEntity.ok(new ApiResponseDTO<>(employeeAdminService.addDataToDataBase(employeeFetchOrCreateRequestList)));
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
            return ResponseEntity.ok(new ApiResponseDTO<>(employeeAdminService.bulkUpdateDataToDataBase(bulkUpdateRequest)));
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
            return ResponseEntity.ok(new ApiResponseDTO<>(employeeAdminService.bulkDeleteDataFromDataBase(bulkDeleteRequestList.getEmployeeIds())));
        } catch (Exception e) {
            logger.error("Deleting records failed . Reason : {} ", e.getMessage());
            throw e;
        }
    }
}
