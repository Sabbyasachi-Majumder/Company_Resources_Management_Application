package com.company.employee.controllers;

import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.EmployeeRequestDTO;
import com.company.employee.dto.EmployeeResponseDTO;
import com.company.employee.repository.EmployeeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeOperationsController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeOperationsController.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final DataSource dataSource;

    // Constructor injection
    public EmployeeOperationsController(EmployeeRepository employeeRepository,
                                        EmployeeService employeeService,
                                        DataSource dataSource) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.dataSource = dataSource;
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
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if (connection.isValid(1)) {
            logger.debug("Testing successful . Database connection is present.");
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Connection to Employee Application is successfully established.", null));
        } else {
            logger.error("Testing failed . Database connection is not present.");
            return ResponseEntity.ok(new ApiResponseDTO<>("error", "Connection to Employee Database failed to be established.", null));
        }
    }

    // Fetching the database table data on request
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
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<EmployeeDTO> pagedData = employeeService.fetchPageData(pageable);
        List<EmployeeDTO> currentData = pagedData.getContent();
        if ((long) size * page > pagedData.getTotalElements())
            return ResponseEntity.ok(new ApiResponseDTO<>("error", "Total number of records is lower than the current page number " + page + " containing " + size + " records each.", null));
        else
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Fetching page " + page + " with " + currentData.size() + " Employee data records", currentData));
    }

    //method to add the employee details to the database
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
            ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
            int addCounter = 0;
            for (EmployeeDTO e : empBean.getEmpDetailsList()) {
                if (!employeeRepository.existsById(e.getEmployeeId())) {
                    logger.debug("Adding employeeId {} ", e.getEmployeeId());
                    employeeService.addData(employeeService.toEntity(e));
                    addCounter++;
                    responses.add(new ApiResponseDTO<>("success", "Successfully added Employee Id " + e.getEmployeeId() + " data records", null));
                } else {
                    logger.error("employeeId {} is already present thus not added again.", e.getEmployeeId());
                    responses.add(new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " already exists ", null));
                }
            }
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Successfully added " + addCounter + " . Add failed : " + (empBean.getEmpDetailsList().size() - addCounter), new EmployeeResponseDTO(null, responses)));

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
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        logger.debug("Searching begins");
        ArrayList<EmployeeDTO> entityArrayList = new ArrayList<>();
        entityArrayList.add(employeeService.toDTO(employeeService.searchData(employeeId)));
        erBean.setEmpDetailsList(entityArrayList);
        logger.debug("Searching employeeId {} ", employeeId);
        return ResponseEntity.ok(new ApiResponseDTO<>("success", "Successfully found Employee Id " + employeeId + " data records", erBean));
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
            ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
            int updateCounter = 0;
            for (EmployeeDTO e : empBean.getEmpDetailsList()) {
                if (employeeRepository.existsById(e.getEmployeeId())) {
                    logger.debug("Updated employeeId {} successfully", e.getEmployeeId());
                    employeeRepository.save(employeeService.toEntity(e));
                    updateCounter++;
                    responses.add(new ApiResponseDTO<>("success", "Successfully updated Employee Id " + e.getEmployeeId() + " data records", null));
                } else {
                    logger.error("Updating employeeId {} failed since employeeId doesn't exist", e.getEmployeeId());
                    responses.add(new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " doesn't exist", null));
                }
            }
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Update Success : " + updateCounter + " . Update Failed : " + (empBean.getEmpDetailsList().size() - updateCounter), new EmployeeResponseDTO(null, responses)));
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
            ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
            int deleteCounter = 0;
            for (EmployeeDTO e : empBean.getEmpDetailsList()) {
                ApiResponseDTO<EmployeeResponseDTO> apiResponse;
                if (employeeRepository.existsById(e.getEmployeeId())) {
                    logger.debug("Deleted employeeId {} successfully", e.getEmployeeId());
                    employeeRepository.deleteById(e.getEmployeeId());
                    deleteCounter++;
                    apiResponse = new ApiResponseDTO<>("success", "Successfully deleted Employee Id " + e.getEmployeeId() + " data records", null);
                } else {
                    logger.error("Deleting employeeId {} failed since employeeId doesn't exist", e.getEmployeeId());
                    apiResponse = new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " doesn't exist", null);
                }
                responses.add(apiResponse);
            }
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Delete Success : " + deleteCounter + ". Delete Failed : " + (empBean.getEmpDetailsList().size() - deleteCounter), new EmployeeResponseDTO(null, responses)));
        } catch (Exception e) {
            logger.error("Deleting records failed . Reason : {} ", e.getMessage());
            throw e;
        }
    }
}
