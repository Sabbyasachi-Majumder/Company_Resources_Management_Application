package com.company.authenticate.controller;

import com.company.authenticate.dto.ApiResponseDTO;
import com.company.authenticate.dto.UserProfileDTO;
import com.company.authenticate.dto.UserProfileRequestDTO;
import com.company.authenticate.dto.UserProfileResponseDTO;
import com.company.authenticate.service.UserProfileService;
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
@RequestMapping("/api/v1/authenticates")
public class UserProfileController {
    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    private final UserProfileService userService;

    // Constructor injection
    public UserProfileController(UserProfileService userService) {
        this.userService = userService;
    }

    public void loggingStart() {
        logger.debug("\n\n\t\t********************* New Request Started ********************\n\n");
    }

    // testing connection
    @GetMapping(value = "/testConnection")
    @Tag(name = "Health Checks")
    @Operation(summary = "Test connection to the application",
            description = "Tests if the connection between the client (e.g., Postman) and the Authenticate service is established.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Connection established successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<String>> testPostmanToApplicationConnection() {
        loggingStart();
        logger.debug("Testing UserOperationsController to Postman connection.");
        return ResponseEntity.ok(new ApiResponseDTO<>("success", "Connection to User Application is successfully established.", null));
    }

    // testing Database connection
    @GetMapping(value = "/testDataBaseConnection")
    @Tag(name = "Health Checks")
    @Operation(summary = "Test database connection",
            description = "Tests if the connection to the user database is established.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Database connection test result",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<String>> testDataBaseConnection() {
        loggingStart();
        logger.debug("Testing UserOperationsController to user database connection.");
        return ResponseEntity.ok(userService.testDatabaseConnection());
    }

    // Fetching the database table data on request with pagination information from users
    @GetMapping(value = "/fetchUsers", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "User Management")
    @Operation(summary = "Fetch all users",
            description = "Retrieves all user records from the database. Requires USER or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<List<UserProfileDTO>>> fetchUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        loggingStart();
        logger.debug("Displaying all users with page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(userService.fetchPagedDataList(pageable));
    }

    //Adding the user details to the database sent by user
    @PostMapping(value = "/addUsers", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "User Management")
    @Operation(summary = "Add new users",
            description = "Adds a list of users to the database. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed or duplicate user ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> addUsers(@Valid @RequestBody UserProfileRequestDTO userBean) {
        loggingStart();
        try {
            logger.debug("Adding all records.");
            return ResponseEntity.ok(userService.addDataToDataBase(userBean.getUserProfileList()));
        } catch (RuntimeException e) {
            logger.error("Adding records failed . Reason {}", e.getMessage());
            return ResponseEntity.ok(new ApiResponseDTO<>("error", e.getMessage(), null));
        }
    }

    // method to search for a user details based on path variable {user id}
    @GetMapping(value = "/searchUser/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "User Management")
    @Operation(summary = "Search user by ID",
            description = "Searches for an user by their ID. Requires USER or ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> searchUser(@PathVariable("userId") int userId) {
        loggingStart();
        logger.debug("Searching userId {} ", userId);
        return ResponseEntity.ok(userService.searchDataBase(userId));
    }

    // method to update the user details based on user id
    @PutMapping(value = "/updateUsers", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "User Management")
    @Operation(summary = "Update user details",
            description = "Updates a list of users in the database. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request: Validation failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> updateUsers(@Valid @RequestBody UserProfileRequestDTO userBean) {
        loggingStart();
        logger.debug("Updating records begins");
        try {
            return ResponseEntity.ok(userService.updateDataToDataBase(userBean.getUserProfileList()));
        } catch (Exception e) {
            logger.error("Updating records failed . Reason : {}", e.getMessage());
            throw e;
        }
    }

    // method to remove the List of user details from the database depending on userId
    @DeleteMapping(value = "/deleteUsers", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Tag(name = "User Management")
    @Operation(summary = "Delete users",
            description = "Deletes a list of users from the database by their IDs. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Authentication required",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden: Insufficient permissions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDTO.class)))
    })
    public ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> deleteUsers(@RequestBody UserProfileRequestDTO userBean) {
        loggingStart();
        logger.debug("Deleting record begins");
        try {
            return ResponseEntity.ok(userService.deleteDataFromDataBase(userBean.getUserProfileList()));
        } catch (Exception e) {
            logger.error("Deleting records failed . Reason : {} ", e.getMessage());
            throw e;
        }
    }
}
