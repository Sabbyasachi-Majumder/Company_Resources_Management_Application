package com.company.department.controllers;

import com.company.department.dto.ApiResponseDTO;
import com.company.department.dto.DepartmentDTO;
import com.company.department.dto.DepartmentRequestDTO;
import com.company.department.dto.DepartmentResponseDTO;
import com.company.department.service.DepartmentService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentOperationsController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentOperationsController.class);
    private final DepartmentService departmentService;

    // Constructor injection
    public DepartmentOperationsController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public void loggingStart() {
        logger.info("\n\n\t\t********************* New Request Started ********************\n\n");
    }

    // testing connection
    @GetMapping(value = "/testConnection")
    @Tag(name = "Health Checks")
    @Operation(summary = "Test connection to the application",
            description = "Tests if the connection between the client (e.g., Postman) and the Department application is established.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection established successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<String>> testPostmanToApplicationConnection() {
        loggingStart();
        logger.info("Testing DepartmentOperationsController to Postman connection.");
        logger.info("DepartmentOperationsController to Postman connection is successfully connected.");
        return ResponseEntity.ok(new ApiResponseDTO<>("success", "Connection to Department Application is successfully established.", null));
    }

    // testing Database connection
    @GetMapping(value = "/testDataBaseConnection")
    @Tag(name = "Health Checks")
    @Operation(summary = "Test database connection",
            description = "Tests if the connection to the department database is established.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database connection test result",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<String>> testDataBaseConnection() {
        loggingStart();
        logger.info("Testing DepartmentOperationsController to department database connection.");
        return ResponseEntity.ok(departmentService.testDatabaseConnection());
    }

    // Fetching the database table data on request with pagination information from users
    @GetMapping(value = "/fetchDepartments", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Department Management")
    @Operation(summary = "Fetch all departments",
            description = "Retrieves all department records from the database. Requires USER or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departments fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<List<DepartmentDTO>>> fetchDepartments(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        loggingStart();
        logger.info("Displaying all departments with page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page-1, size);
        return ResponseEntity.ok(departmentService.fetchPagedDataList(pageable));
    }

    //Adding the department details to the database sent by user
    @PostMapping(value = "/addDepartments", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Department Management")
    @Operation(summary = "Add new departments",
            description = "Adds a list of departments to the database. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departments added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed or duplicate department ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> addDepartments(@Valid @RequestBody DepartmentRequestDTO departmentBean) {
        loggingStart();
        try {
            logger.info("Adding all records.");
            return ResponseEntity.ok(departmentService.addDataToDataBase(departmentBean.getDepartmentDetailList()));
        } catch (RuntimeException e) {
            logger.error("Adding records failed . Reason {}", e.getMessage());
            return ResponseEntity.ok(new ApiResponseDTO<>("error", e.getMessage(), null));
        }
    }

    // method to search for a department details based on path variable {department id}
    @GetMapping(value = "/searchDepartment/{departmentId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Department Management")
    @Operation(summary = "Search department by ID",
            description = "Searches for an department by their ID. Requires USER or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> searchDepartment(@PathVariable("departmentId") int departmentId) {
        loggingStart();
        logger.info("Searching departmentId {} ", departmentId);
        return ResponseEntity.ok(departmentService.searchDataBase(departmentId));
    }

    // method to update the department details based on department id
    @PutMapping(value = "/updateDepartments", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Department Management")
    @Operation(summary = "Update department details",
            description = "Updates a list of departments in the database. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departments updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> updateDepartments(@Valid @RequestBody DepartmentRequestDTO departmentBean) {
        loggingStart();
        logger.info("Updating records begins");
        try {
            return ResponseEntity.ok(departmentService.updateDataToDataBase(departmentBean.getDepartmentDetailList()));
        } catch (Exception e) {
            logger.error("Updating records failed . Reason : {}", e.getMessage());
            throw e;
        }
    }

    // method to remove the List of department details from the database depending on departmentId
    @DeleteMapping(value = "/deleteDepartments", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Department Management")
    @Operation(summary = "Delete departments",
            description = "Deletes a list of departments from the database by their IDs. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departments deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Department not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<DepartmentResponseDTO>> deleteDepartments(@RequestBody DepartmentRequestDTO departmentBean) {
        loggingStart();
        logger.info("Deleting record begins");
        try {
            return ResponseEntity.ok(departmentService.deleteDataFromDataBase(departmentBean.getDepartmentDetailList()));
        } catch (Exception e) {
            logger.error("Deleting records failed . Reason : {} ", e.getMessage());
            throw e;
        }
    }
}
