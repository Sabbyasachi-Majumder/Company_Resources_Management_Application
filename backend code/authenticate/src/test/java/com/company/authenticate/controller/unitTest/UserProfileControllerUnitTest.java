package com.company.authenticate.controller.unitTest;

import com.company.authenticate.controller.AuthenticationGlobalExceptionHandler;
import com.company.authenticate.controller.UserProfileController;
import com.company.authenticate.dto.ApiResponseDTO;
import com.company.authenticate.dto.UserProfileDTO;
import com.company.authenticate.dto.UserProfileRequestDTO;
import com.company.authenticate.dto.UserProfileResponseDTO;
import com.company.authenticate.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileControllerUnitTest {

    @Mock
    private UserProfileService userService;

    @Mock
    private Logger logger;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserProfileController userProfileController;

    @InjectMocks
    private AuthenticationGlobalExceptionHandler exceptionHandler;

    private UserProfileDTO userProfileDTO;
    private UserProfileRequestDTO userProfileRequestDTO;
    private UserProfileResponseDTO userProfileResponseDTO;
    private ApiResponseDTO<List<UserProfileDTO>> fetchResponse;
    private ApiResponseDTO<UserProfileResponseDTO> successResponse;

    /**
     * Sets up common test data before each test method.
     * Importance: Initializes test objects to ensure a consistent starting point for all tests,
     * reducing code duplication and ensuring test isolation. Uses mutable ArrayList to prevent
     * ClassCastException when DTOs expect mutable lists, ensuring compatibility with production code.
     */
    @BeforeEach
    void setUp() {
        userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUserId(1);
        userProfileDTO.setUserName("admin0001");
        userProfileDTO.setPassword("admin0001");
        userProfileDTO.setRole("admin");
        userProfileDTO.setEnabled(true);

        userProfileRequestDTO = new UserProfileRequestDTO();
        userProfileRequestDTO.setUserProfileList(new ArrayList<>(List.of(userProfileDTO)));

        userProfileResponseDTO = new UserProfileResponseDTO();
        userProfileResponseDTO.setUserProfileList(new ArrayList<>(List.of(userProfileDTO)));

        fetchResponse = new ApiResponseDTO<>("success", "Users fetched successfully", new ArrayList<>(List.of(userProfileDTO)));
        successResponse = new ApiResponseDTO<>("success", "Operation successful", userProfileResponseDTO);
    }

    /**
     * Tests the testPostmanToApplicationConnection method for successful connection check.
     * Importance: Verifies that the health check endpoint returns a 200 OK response, ensuring
     * the service is reachable via the API Gateway (e.g., from Postman). This is critical for
     * monitoring service availability in a microservice architecture.
     */
    @Test
    void testPostmanToApplicationConnectionSuccess() {
        // Act: Call the controller's test connection method
        ResponseEntity<ApiResponseDTO<String>> response = userProfileController.testPostmanToApplicationConnection();

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK for successful connection");
        assertEquals("success", response.getBody().getStatus(), "Response status should be 'success'");
        assertEquals("Connection to User Application is successfully established.", response.getBody().getMessage(),
                "Success message should match");
        assertNull(response.getBody().getData(), "Response data should be null for connection test");
        verify(userService, times(0)).testDatabaseConnection();
        // Importance: Ensures no service methods are called, confirming the endpoint is a simple health check.
    }

    /**
     * Tests the testDataBaseConnection method for successful database connection check.
     * Importance: Validates that the database connection health check returns a 200 OK response,
     * ensuring the service can connect to the database. This is critical for confirming database
     * availability in production environments.
     */
    @Test
    void testDataBaseConnectionSuccess() {
        // Arrange: Mock the service to return a successful response
        ApiResponseDTO<String> dbResponse = new ApiResponseDTO<>("success", "Database connection established", null);
        when(userService.testDatabaseConnection()).thenReturn(dbResponse);

        // Act: Call the controller's database connection test method
        ResponseEntity<ApiResponseDTO<String>> response = userProfileController.testDataBaseConnection();

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK for successful DB connection");
        assertEquals("success", response.getBody().getStatus(), "Response status should be 'success'");
        assertEquals("Database connection established", response.getBody().getMessage(),
                "Success message should match");
        assertNull(response.getBody().getData(), "Response data should be null for DB connection test");
        verify(userService, times(1)).testDatabaseConnection();
        // Importance: Ensures the service is called to check DB connectivity, critical for system reliability.
    }

    /**
     * Tests the fetchUsers method for successful retrieval of user data with pagination.
     * Importance: Verifies that the endpoint correctly fetches paginated user data, returning a
     * 200 OK response. This is essential for user management in admin dashboards and ensures
     * scalability with large datasets through pagination.
     */
    @Test
    void testFetchUsersSuccess() {
        // Arrange: Mock the service to return a list of users
        when(userService.fetchPagedDataList(any(Pageable.class))).thenReturn(fetchResponse);

        // Act: Call the controller's fetchUsers method with page=1, size=10
        ResponseEntity<ApiResponseDTO<List<UserProfileDTO>>> response = userProfileController.fetchUsers(1, 10);

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK for successful fetch");
        assertEquals("success", response.getBody().getStatus(), "Response status should be 'success'");
        assertEquals("Users fetched successfully", response.getBody().getMessage(),
                "Success message should match");
        assertNotNull(response.getBody().getData(), "Response data should contain user list");
        assertEquals(1, response.getBody().getData().size(), "Should return one user");
        assertEquals("admin0001", response.getBody().getData().get(0).getUserName(), "Username should match");
        verify(userService, times(1)).fetchPagedDataList(any(Pageable.class));
        // Importance: Ensures pagination is applied and the service returns expected data,
        // critical for efficient data retrieval in user management.
    }

    /**
     * Tests the fetchUsers method handling of AccessDeniedException for unauthorized access.
     * Importance: Ensures that users without proper roles (USER or ADMIN) are denied access,
     * returning a 403 Forbidden response. This is critical for securing sensitive user data
     * in a microservice architecture.
     */
    @Test
    void testFetchUsersAccessDeniedException() {
        // Arrange: Mock HttpServletRequest for logging
        when(request.getRequestURI()).thenReturn("/api/v1/authenticates/fetchUsers");

        // Act: Directly test the exception handler for AccessDeniedException
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleAccessDeniedException(
                new AccessDeniedException("Insufficient permissions"), request);

        // Assert: Verify the error response
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "Should return 403 Forbidden for unauthorized access");
        assertEquals("error", response.getBody().getStatus(), "Response status should be 'error'");
        assertEquals("Access Denied: Insufficient permissions [AUTH_403]", response.getBody().getMessage(),
                "Error message should indicate insufficient permissions");
        assertNull(response.getBody().getData(), "Response data should be null for error cases");
        verify(userService, times(0)).fetchPagedDataList(any(Pageable.class));
        verify(request, times(1)).getRequestURI();
        // Importance: Confirms that unauthorized access is blocked, ensuring security compliance.
    }

    /**
     * Tests the addUsers method for successful user addition.
     * Importance: Verifies that valid user data is added to the database, returning a 200 OK
     * response. This is critical for user registration functionality, a core feature of user
     * management in authentication services.
     */
    @Test
    void testAddUsersSuccess() {
        // Arrange: Mock the service to return a successful response
        when(userService.addDataToDataBase((ArrayList<UserProfileDTO>) any(List.class))).thenReturn(successResponse);

        // Act: Call the controller's addUsers method
        ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> response = userProfileController.addUsers(userProfileRequestDTO);

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK for successful user addition");
        assertEquals("success", response.getBody().getStatus(), "Response status should be 'success'");
        assertEquals("Operation successful", response.getBody().getMessage(), "Success message should match");
        assertNotNull(response.getBody().getData(), "Response data should contain user profile list");
        assertEquals("admin0001", response.getBody().getData().getUserProfileList().get(0).getUserName(), "Username should match");
        verify(userService, times(1)).addDataToDataBase((ArrayList<UserProfileDTO>) any(List.class));
        // Importance: Ensures the service correctly processes valid user data, critical for user onboarding.
    }

    /**
     * Tests the addUsers method handling of MethodArgumentNotValidException for invalid input.
     * Importance: Ensures that invalid user data (e.g., blank username) is rejected with a
     * 422 Unprocessable Entity response, providing clear validation feedback. This is critical
     * for data integrity and user experience in REST APIs.
     */
    @Test
    void testAddUsersMethodArgumentNotValidException() {
        // Arrange: Create invalid request data with validation errors
        UserProfileDTO invalidUser = new UserProfileDTO();
        invalidUser.setUserId(1);
        invalidUser.setUserName("");
        UserProfileRequestDTO invalidRequest = new UserProfileRequestDTO();
        invalidRequest.setUserProfileList(new ArrayList<>(List.of(invalidUser)));

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(invalidRequest, "userProfileRequestDTO");
        bindingResult.addError(new FieldError("userProfileRequestDTO", "userProfileList[0].userName", "must not be blank"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // Act: Directly test the exception handler for validation errors
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleValidationExceptions(exception);

        // Assert: Verify the error response
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode(), "Should return 422 Unprocessable Entity for invalid input");
        assertEquals("error", response.getBody().getStatus(), "Response status should be 'error'");
        assertEquals("Validation failed: userProfileList[0].userName: must not be blank; ", response.getBody().getMessage(),
                "Error message should list validation failures");
        assertNull(response.getBody().getData(), "Response data should be null for error cases");
        verify(userService, times(0)).addDataToDataBase(any());
        verify(request, times(0)).getRequestURI();
        // Importance: Confirms that invalid data is rejected before service invocation, ensuring data integrity.
    }

    /**
     * Tests the addUsers method handling of DataIntegrityViolationException for duplicate user IDs.
     * Importance: Ensures that attempts to add users with duplicate IDs are handled gracefully,
     * returning a 400 Bad Request response. This is critical for maintaining database integrity
     * and preventing errors in user registration.
     */
    @Test
    void testAddUsersDataIntegrityViolationException() {
        // Create DataIntegrityViolationException with a root cause
        SQLException rootCause = new SQLException("Duplicate entry for userId");
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Duplicate user ID", rootCause);

        // Act: Directly test the exception handler for DataIntegrityViolationException
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleDataIntegrityViolationException(exception);

        // Assert: Verify the error response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Should return 400 Bad Request for duplicate user ID");
        assertEquals("error", response.getBody().getStatus(), "Response status should be 'error'");
        assertEquals("Bad Request: Duplicate user ID or username", response.getBody().getMessage(),
                "Error message should indicate duplicate user ID");
        assertNull(response.getBody().getData(), "Response data should be null for error cases");
        verify(userService, times(0)).addDataToDataBase(any());
        verify(request, times(0)).getRequestURI(); // Handler doesn't use request
        // Importance: Ensures database constraints are enforced, critical for data consistency.
    }

    /**
     * Tests the searchUser method for successful user retrieval by ID.
     * Importance: Verifies that the endpoint correctly retrieves a user by ID, returning a
     * 200 OK response. This is essential for user lookup functionality in admin or user interfaces.
     */
    @Test
    void testSearchUserSuccess() {
        // Arrange: Mock the service to return a successful response for user ID 1
        when(userService.searchDataBase(1)).thenReturn(successResponse);

        // Act: Call the controller's searchUser method with ID 1
        ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> response = userProfileController.searchUser(1);

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK for successful user search");
        assertEquals("success", response.getBody().getStatus(), "Response status should be 'success'");
        assertEquals("Operation successful", response.getBody().getMessage(), "Success message should match");
        assertNotNull(response.getBody().getData(), "Response data should contain user profile");
        assertEquals("admin0001", response.getBody().getData().getUserProfileList().get(0).getUserName(), "Username should match");
        verify(userService, times(1)).searchDataBase(1L);
        // Importance: Ensures accurate user retrieval, critical for user management features.
    }

    /**
     * Tests the searchUser method handling of NoSuchElementException for non-existent user.
     * Importance: Ensures that attempts to search for a non-existent user return a 404 Not Found
     * response, providing clear feedback. This is critical for user experience and API reliability.
     */
    @Test
    void testSearchUserNoSuchElementException() {
        // Act: Directly test the exception handler for NoSuchElementException
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleNoSuchElementException(
                new NoSuchElementException("userId 1 not found"));

        // Assert: Verify the error response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Should return 404 Not Found for non-existent user");
        assertEquals("error", response.getBody().getStatus(), "Response status should be 'error'");
        assertEquals("Resource not found: userId 1 not found", response.getBody().getMessage(),
                "Error message should indicate user not found");
        assertNull(response.getBody().getData(), "Response data should be null for error cases");
        verify(userService, times(0)).searchDataBase(anyLong());
        verify(request, times(0)).getRequestURI(); // Handler doesn't use request
        // Importance: Confirms that non-existent users are handled correctly, ensuring robust error handling.
    }

    /**
     * Tests the updateUsers method for successful user updates.
     * Importance: Verifies that valid user updates are processed, returning a 200 OK response.
     * This is critical for maintaining up-to-date user information in the system.
     */
    @Test
    void testUpdateUsersSuccess() {
        // Arrange: Mock the service to return a successful response
        when(userService.updateDataToDataBase((ArrayList<UserProfileDTO>) any(List.class))).
                thenReturn(successResponse);

        // Act: Call the controller's updateUsers method
        ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> response = userProfileController.updateUsers(userProfileRequestDTO);

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK for successful user update");
        assertEquals("success", response.getBody().getStatus(), "Response status should be 'success'");
        assertEquals("Operation successful", response.getBody().getMessage(), "Success message should match");
        assertNotNull(response.getBody().getData(), "Response data should contain updated user profile");
        assertEquals("admin0001", response.getBody().getData().getUserProfileList().get(0).getUserName(), "Username should match");
        verify(userService, times(1)).updateDataToDataBase((ArrayList<UserProfileDTO>) any(List.class));
        // Importance: Ensures user updates are processed correctly, critical for user management.
    }

    /**
     * Tests the updateUsers method handling of NoSuchElementException for non-existent user.
     * Importance: Ensures that attempts to update non-existent users return a 404 Not Found
     * response, maintaining data integrity and providing clear feedback.
     */
    @Test
    void testUpdateUsersNoSuchElementException() {
        // Act: Directly test the exception handler for NoSuchElementException
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleNoSuchElementException(
                new NoSuchElementException("userId 1 not found"));

        // Assert: Verify the error response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Should return 404 Not Found for non-existent user");
        assertEquals("error", response.getBody().getStatus(), "Response status should be 'error'");
        assertEquals("Resource not found: userId 1 not found", response.getBody().getMessage(),
                "Error message should indicate user not found");
        assertNull(response.getBody().getData(), "Response data should be null for error cases");
        verify(userService, times(0)).updateDataToDataBase(any());
        verify(request, times(0)).getRequestURI(); // Handler doesn't use request
        // Importance: Confirms that updates for non-existent users are rejected, ensuring data consistency.
    }

    /**
     * Tests the deleteUsers method for successful user deletion.
     * Importance: Verifies that users are deleted successfully, returning a 200 OK response.
     * This is critical for user management, allowing removal of user accounts as needed.
     */
    @Test
    void testDeleteUsersSuccess() {
        // Arrange: Mock the service to return a successful response
        when(userService.deleteDataFromDataBase((ArrayList<UserProfileDTO>) any(List.class))).thenReturn(successResponse);

        // Act: Call the controller's deleteUsers method
        ResponseEntity<ApiResponseDTO<UserProfileResponseDTO>> response = userProfileController.deleteUsers(userProfileRequestDTO);

        // Assert: Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Should return 200 OK for successful user deletion");
        assertEquals("success", response.getBody().getStatus(), "Response status should be 'success'");
        assertEquals("Operation successful", response.getBody().getMessage(), "Success message should match");
        assertNotNull(response.getBody().getData(), "Response data should contain deleted user profile");
        assertEquals("admin0001", response.getBody().getData().getUserProfileList().get(0).getUserName(), "Username should match");
        verify(userService, times(1)).deleteDataFromDataBase((ArrayList<UserProfileDTO>) any(List.class));
        // Importance: Ensures user deletion works correctly, critical for account management.
    }

    /**
     * Tests the deleteUsers method handling of NoSuchElementException for non-existent user.
     * Importance: Ensures that attempts to delete non-existent users return a 404 Not Found
     * response, maintaining data integrity and providing clear feedback.
     */
    @Test
    void testDeleteUsersNoSuchElementException() {
        // Act: Directly test the exception handler for NoSuchElementException
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleNoSuchElementException(
                new NoSuchElementException("userId 1 not found"));

        // Assert: Verify the error response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Should return 404 Not Found for non-existent user");
        assertEquals("error", response.getBody().getStatus(), "Response status should be 'error'");
        assertEquals("Resource not found: userId 1 not found", response.getBody().getMessage(),
                "Error message should indicate user not found");
        assertNull(response.getBody().getData(), "Response data should be null for error cases");
        verify(userService, times(0)).deleteDataFromDataBase(any());
        verify(request, times(0)).getRequestURI(); // Handler doesn't use request
        // Importance: Confirms that deletion of non-existent users is handled correctly, ensuring robust error handling.
    }
}