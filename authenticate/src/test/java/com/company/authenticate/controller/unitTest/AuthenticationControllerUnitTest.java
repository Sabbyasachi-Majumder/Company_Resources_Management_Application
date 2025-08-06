package com.company.authenticate.controller.unitTest;

import com.company.authenticate.controller.AuthenticationController;
import com.company.authenticate.controller.AuthenticationGlobalExceptionHandler;
import com.company.authenticate.dto.ApiResponseDTO;
import com.company.authenticate.dto.AuthRequestDTO;
import com.company.authenticate.dto.AuthResponseDTO;
import com.company.authenticate.service.AuthenticateServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerUnitTest {

    @Mock
    private AuthenticateServiceImpl authenticateService;

    @Mock
    private Logger logger;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthenticationController authenticationController;

    @InjectMocks
    private AuthenticationGlobalExceptionHandler exceptionHandler;

    private AuthRequestDTO authRequestDTO;
    private AuthResponseDTO authResponseDTO;
    private ApiResponseDTO<AuthResponseDTO> successResponse;

    /**
     * Sets up common test data before each test method.
     * Importance: Initializes test objects to ensure a consistent starting point for all tests,
     * reducing code duplication and ensuring isolation between test cases. This is critical
     * for maintaining reliable and repeatable tests, a key requirement in industry-standard
     * unit testing.
     */
    @BeforeEach
    void setUp() {
        authRequestDTO = new AuthRequestDTO("admin1", "admin1");

        authResponseDTO = new AuthResponseDTO();
        authResponseDTO.setToken("eyJhbGciOiJIUzM4NCJ9...");
        authResponseDTO.setRefreshToken("eyJhbGciOiJIUzM4NCJ9...");

        successResponse = new ApiResponseDTO<>("success", "Login successful for user admin1", authResponseDTO);
    }

    /**
     * Tests successful authentication with valid credentials.
     * Importance: Verifies the core functionality of the authentication service, ensuring
     * that valid username/password combinations result in a 200 OK response with valid JWT
     * tokens. This is critical for confirming the service's primary purpose—authenticating
     * users and generating tokens for secure access in a microservice architecture.
     */
    @Test
    void testAuthenticateSuccess() {
        // Arrange
        when(authenticateService.authenticateUser(anyString(), anyString())).thenReturn(successResponse);

        // Act
        ResponseEntity<ApiResponseDTO<AuthResponseDTO>> response = authenticationController.authenticate(authRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Login successful for user admin1", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(authResponseDTO.getToken(), response.getBody().getData().getToken());
        verify(authenticateService, times(1)).authenticateUser("admin1", "admin1");
    }

    /**
     * Tests handling of BadCredentialsException for invalid credentials.
     * Importance: Ensures the system correctly handles invalid username/password attempts,
     * returning a 401 Unauthorized response with a clear error message. This is critical
     * for security, as it prevents unauthorized access and provides user-friendly feedback,
     * aligning with industry standards for secure APIs.
     */
    @Test
    void testAuthenticateBadCredentialsException() {
        // Arrange
        // No stubbing for authenticateService.authenticateUser since it’s not called
        when(request.getRequestURI()).thenReturn("/api/v1/authenticates/authenticate");

        // Act
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleAuthenticationException(
                new BadCredentialsException("Invalid username or password"), request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Unauthorized: Invalid username or password [AUTH_401_INVALID_CREDENTIALS]", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(authenticateService, times(0)).authenticateUser(anyString(), anyString());
        verify(request, times(1)).getRequestURI();
    }

    /**
     * Tests handling of DisabledException for disabled user accounts.
     * Importance: Validates that the system prevents login for disabled accounts, returning
     * a 403 Forbidden response. This is essential for enforcing account status policies,
     * a common requirement in secure authentication systems to prevent unauthorized access
     * by disabled users.
     */
    @Test
    void testAuthenticateDisabledException() {
        // Arrange
        // No stubbing for authenticateService.authenticateUser since it’s not called
        when(request.getRequestURI()).thenReturn("/api/v1/authenticates/authenticate");

        // Act
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleDisabledException(
                new DisabledException("User account is disabled"), request);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Forbidden: User account is disabled [AUTH_403_DISABLED]", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(authenticateService, times(0)).authenticateUser(anyString(), anyString());
        verify(request, times(1)).getRequestURI();
    }

    /**
     * Tests handling of AuthenticationCredentialsNotFoundException for missing credentials.
     * Importance: Verifies that the system correctly handles cases where authentication
     * credentials are missing, returning a 401 Unauthorized response. This is critical
     * for securing the API against requests lacking proper authentication data, a key
     * aspect of robust security in microservices.
     */
    @Test
    void testAuthenticateAuthenticationCredentialsNotFoundException() {
        // Arrange
        // No stubbing for authenticateService.authenticateUser since it’s not called
        when(request.getRequestURI()).thenReturn("/api/v1/authenticates/authenticate");

        // Act
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleAuthenticationException(
                new AuthenticationCredentialsNotFoundException("Authentication required"), request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Unauthorized: Authentication required [AUTH_401_NO_CREDENTIALS]", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(authenticateService, times(0)).authenticateUser(anyString(), anyString());
        verify(request, times(1)).getRequestURI();
    }

    /**
     * Tests handling of MethodArgumentNotValidException for invalid input data.
     * Importance: Ensures the system validates input data (e.g., blank username or password),
     * returning a 422 Unprocessable Entity response with detailed error messages. This is
     * critical for user experience and API reliability, as it prevents processing invalid
     * requests and provides clear feedback, aligning with REST API best practices.
     */
    @Test
    void testAuthenticateMethodArgumentNotValidException() {
        // Arrange
        AuthRequestDTO invalidRequest = new AuthRequestDTO("", "");

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(invalidRequest, "authRequestDTO");
        bindingResult.addError(new FieldError("authRequestDTO", "userName", "must not be blank"));
        bindingResult.addError(new FieldError("authRequestDTO", "password", "must not be blank"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleValidationExceptions(exception);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Validation failed: userName: must not be blank; password: must not be blank; ", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(authenticateService, times(0)).authenticateUser(anyString(), anyString());
        verify(request, times(0)).getRequestURI();
    }

    /**
     * Tests handling of generic AuthenticationException for other authentication failures.
     * Importance: Verifies that the system handles unexpected authentication errors
     * gracefully, returning a 401 Unauthorized response with a generic error message.
     * This is critical for robustness, ensuring the system can handle unforeseen issues
     * without exposing sensitive details, a key security practice.
     */
    @Test
    void testAuthenticateGenericAuthenticationException() {
        // Arrange
        // No stubbing for authenticateService.authenticateUser since it’s not called
        when(request.getRequestURI()).thenReturn("/api/v1/authenticates/authenticate");

        // Act
        ResponseEntity<ApiResponseDTO<String>> response = exceptionHandler.handleAuthenticationException(
                new AuthenticationException("Authentication failed") {
                }, request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Unauthorized: Authentication failed [AUTH_401_GENERIC]", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(authenticateService, times(0)).authenticateUser(anyString(), anyString());
        verify(request, times(1)).getRequestURI();
    }
}