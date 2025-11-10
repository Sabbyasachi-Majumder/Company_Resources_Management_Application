package com.company.project.controllers;

import com.company.project.dto.ApiResponseDTO;
import com.company.project.dto.ProjectDTO;
import com.company.project.dto.ProjectRequestDTO;
import com.company.project.dto.ProjectResponseDTO;
import com.company.project.service.ProjectService;

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
@RequestMapping("/api/v1/project")
public class ProjectOperationsController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectOperationsController.class);
    private final ProjectService projectService;

    // Constructor injection
    public ProjectOperationsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    public void loggingStart() {
        logger.info("\n\n\t\t********************* New Request Started ********************\n\n");
    }

    // testing connection
    @GetMapping(value = "/testConnection")
    @Tag(name = "Health Checks")
    @Operation(summary = "Test connection to the application",
            description = "Tests if the connection between the client (e.g., Postman) and the Project application is established.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection established successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<String>> testPostmanToApplicationConnection() {
        loggingStart();
        logger.debug("Testing ProjectOperationsController to Postman connection.");
        return ResponseEntity.ok(new ApiResponseDTO<>("success", "Connection to Project Application is successfully established.", null));
    }

    // testing Database connection
    @GetMapping(value = "/testDataBaseConnection")
    @Tag(name = "Health Checks")
    @Operation(summary = "Test database connection",
            description = "Tests if the connection to the project database is established.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database connection test result",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<String>> testDataBaseConnection() {
        loggingStart();
        logger.debug("Testing ProjectOperationsController to project database connection.");
        return ResponseEntity.ok(projectService.testDatabaseConnection());
    }

    // Fetching the database table data on request with pagination information from users
    @GetMapping(value = "/fetchProjects", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Project Management")
    @Operation(summary = "Fetch all projects",
            description = "Retrieves all project records from the database. Requires USER or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<List<ProjectDTO>>> fetchProjects(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        loggingStart();
        logger.debug("Displaying all projects with page: {}, size: {}", page, size);
        return ResponseEntity.ok(projectService.fetchPagedDataList(page,size));
    }

    //Adding the project details to the database sent by user
    @PostMapping(value = "/addProjects", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Project Management")
    @Operation(summary = "Add new projects",
            description = "Adds a list of projects to the database. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed or duplicate project ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<ProjectResponseDTO>> addProjects(@Valid @RequestBody ProjectRequestDTO empBean) {
        loggingStart();
        try {
            logger.debug("Adding all records.");
            return ResponseEntity.ok(projectService.addDataToDataBase(empBean.getPrjDetailsList()));
        } catch (RuntimeException e) {
            logger.error("Adding records failed . Reason {}", e.getMessage());
            return ResponseEntity.ok(new ApiResponseDTO<>("error", e.getMessage(), null));
        }
    }

    // method to search for a project details based on path variable {project id}
    @GetMapping(value = "/searchProject/{projectId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Project Management")
    @Operation(summary = "Search project by ID",
            description = "Searches for an project by their ID. Requires USER or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<ProjectResponseDTO>> searchProject(@PathVariable("projectId") int projectId) {
        loggingStart();
        logger.debug("Searching projectId {} ", projectId);
        return ResponseEntity.ok(projectService.searchDataBase(projectId));
    }

    // method to update the project details based on project id
    @PutMapping(value = "/updateProjects", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Project Management")
    @Operation(summary = "Update project details",
            description = "Updates a list of projects in the database. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<ProjectResponseDTO>> updateProjects(@Valid @RequestBody ProjectRequestDTO empBean) {
        loggingStart();
        logger.debug("Updating records begins");
        try {
            return ResponseEntity.ok(projectService.updateDataToDataBase(empBean.getPrjDetailsList()));
        } catch (Exception e) {
            logger.error("Updating records failed . Reason : {}", e.getMessage());
            throw e;
        }
    }

    // method to remove the List of project details from the database depending on projectId
    @DeleteMapping(value = "/deleteProjects", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "Project Management")
    @Operation(summary = "Delete projects",
            description = "Deletes a list of projects from the database by their IDs. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<ProjectResponseDTO>> deleteProjects(@RequestBody ProjectRequestDTO empBean) {
        loggingStart();
        logger.debug("Deleting record begins");
        try {
            return ResponseEntity.ok(projectService.deleteDataFromDataBase(empBean.getPrjDetailsList()));
        } catch (Exception e) {
            logger.error("Deleting records failed . Reason : {} ", e.getMessage());
            throw e;
        }
    }
}
