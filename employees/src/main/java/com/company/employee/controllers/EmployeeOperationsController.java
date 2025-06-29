package com.company.employee.controllers;

import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.EmployeeRequestDTO;
import com.company.employee.dto.EmployeeResponseDTO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeOperationsController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeOperationsController.class);
    private final EmployeeService employeeService;

    // Constructor injection
    public EmployeeOperationsController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public void loggingStart() {
        logger.info("\n\n\t\t********************* New Request Started ********************\n\n");
    }

    // testing connection
    @GetMapping(value = "/testConnection")
    @Tag(name = "Health Checks")
    @Operation(summary = "Test connection to the application",
            description = "Tests if the connection between the client (e.g., Postman) and the Employee application is established.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection established successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<String>> testPostmanToApplicationConnection() {
        loggingStart();
        logger.debug("Testing EmployeeOperationsController to Postman connection.");
        return ResponseEntity.ok(new ApiResponseDTO<>("success", "Connection to Employee Application is successfully established.", null));
    }

    // testing Database connection
    @GetMapping(value = "/testDataBaseConnection")
    @Tag(name = "Health Checks")
    @Operation(summary = "Test database connection",
            description = "Tests if the connection to the employee database is established.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database connection test result",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<String>> testDataBaseConnection() throws SQLException {
        loggingStart();
        logger.debug("Testing EmployeeOperationsController to employee database connection.");
        return ResponseEntity.ok(employeeService.testDatabaseConnection());
    }

    // Fetching the database table data on request with pagination information from users
    @GetMapping(value = "/fetchEmployees", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Fetch all employees",
            description = "Retrieves all employee records from the database. Requires USER or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<List<EmployeeDTO>>> fetchEmployees(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        loggingStart();
        logger.debug("Displaying all employees with page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(employeeService.fetchPagedDataList(pageable));
    }

    //Adding the employee details to the database sent by user
    @PostMapping(value = "/addEmployees", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Add new employees",
            description = "Adds a list of employees to the database. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed or duplicate employee ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> addEmployees(@Valid @RequestBody EmployeeRequestDTO empBean) {
        loggingStart();
        try {
            logger.debug("Adding all records.");
            return ResponseEntity.ok(employeeService.addDataToDatBase(empBean.getEmpDetailsList()));
        } catch (RuntimeException e) {
            logger.error("Adding records failed . Reason {}", e.getMessage());
            return ResponseEntity.ok(new ApiResponseDTO<>("error", e.getMessage(), null));
        }
    }

    // method to search for an employee details based on path variable {employee id}
    @GetMapping(value = "/searchEmployee/{employeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Search employee by ID",
            description = "Searches for an employee by their ID. Requires USER or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> searchEmployee(@PathVariable("employeeId") int employeeId) {
        loggingStart();
        logger.debug("Searching employeeId {} ", employeeId);
        return ResponseEntity.ok(employeeService.searchDataBase(employeeId));
    }

    // method to update the employee details based on employee id
    @PutMapping(value = "/updateEmployees", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Update employee details",
            description = "Updates a list of employees in the database. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> updateEmployees(@Valid @RequestBody EmployeeRequestDTO empBean) {
        loggingStart();
        logger.debug("Updating records begins");
        try {
            return ResponseEntity.ok(employeeService.updateDataToDataBase(empBean.getEmpDetailsList()));
        } catch (Exception e) {
            logger.error("Updating records failed . Reason : {}", e.getMessage());
            throw e;
        }
    }

    // method to remove the List of employee details from the database depending on employeeId
    @DeleteMapping(value = "/deleteEmployees", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Employee Management")
    @Operation(summary = "Delete employees",
            description = "Deletes a list of employees from the database by their IDs. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employees deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> deleteEmployees(@RequestBody EmployeeRequestDTO empBean) {
        loggingStart();
        logger.debug("Deleting record begins");
        try {
            return ResponseEntity.ok(employeeService.deleteDataFromDataBase(empBean.getEmpDetailsList()));
        } catch (Exception e) {
            logger.error("Deleting records failed . Reason : {} ", e.getMessage());
            throw e;
        }
    }
}
