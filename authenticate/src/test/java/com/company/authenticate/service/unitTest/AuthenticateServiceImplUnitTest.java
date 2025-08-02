package com.company.authenticate.service.unitTest;

import com.company.authenticate.dto.ApiResponseDTO;
import com.company.authenticate.dto.AuthResponseDTO;
import com.company.authenticate.security.JwtUtil;
import com.company.authenticate.service.AuthenticateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticateServiceImplUnitTest {

    @Mock
    public AuthenticationManager authenticationManager;
    @Mock
    public JwtUtil jwtUtil;

    @InjectMocks
    private AuthenticateServiceImpl authenticateService; // Class under test with mocked dependencies injected

    private UserDetails userDetails; // Reusable UserDetails for tests

    /**
     * Tests successful authentication with valid credentials.
     * Verifies that the service returns a success response with access and refresh tokens.
     */
    @Test
    void testAuthenticateUserSuccess() {
        // Arrange: Mock AuthenticationManager and JwtUtil behavior
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("accessToken");
        when(jwtUtil.generateRefreshToken(userDetails)).thenReturn("refreshToken");

        // Act: Call the authenticateUser method
        ApiResponseDTO<AuthResponseDTO> response = authenticateService.authenticateUser("testuser", "password");

        // Assert: Verify the response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Login successful for user testuser", response.getMessage(), "Message should indicate success");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals("accessToken", response.getData().getToken(), "Access token should match");
        assertEquals("refreshToken", response.getData().getRefreshToken(), "Refresh token should match");

        // Verify: Ensure dependencies were called correctly
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(userDetails);
        verify(jwtUtil).generateRefreshToken(userDetails);
    }

    /**
     * Tests authentication failure due to invalid credentials.
     * Verifies that BadCredentialsException is thrown.
     */
    @Test
    void testAuthenticateUserInvalidCredentials() {
        // Arrange: Mock AuthenticationManager to throw BadCredentialsException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        // Act & Assert: Verify that the exception is thrown
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authenticateService.authenticateUser("testuser", "wrongPassword");
        }, "Should throw BadCredentialsException for invalid credentials");

        assertEquals("Invalid username or password", exception.getMessage(), "Exception message should match");

        // Verify: Ensure AuthenticationManager was called, but JwtUtil was not
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Tests authentication failure due to a disabled user account.
     * Verifies that DisabledException is thrown.
     */
    @Test
    void testAuthenticateUserDisabledAccount() {
        // Arrange: Mock AuthenticationManager to throw DisabledException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DisabledException("User account is disabled"));

        // Act & Assert: Verify that the exception is thrown
        DisabledException exception = assertThrows(DisabledException.class, () -> {
            authenticateService.authenticateUser("disabledUser", "validPassword");
        }, "Should throw DisabledException for disabled user");

        assertEquals("User account is disabled", exception.getMessage(), "Exception message should match");

        // Verify: Ensure AuthenticationManager was called, but JwtUtil was not
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Tests authentication with null username.
     * Verifies that IllegalArgumentException or AuthenticationException is thrown.
     */
    @Test
    void testAuthenticateUserNullUsername() {
        // Arrange: Mock AuthenticationManager to throw an exception for null username
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new IllegalArgumentException("Username cannot be null"));

        // Act & Assert: Verify that the exception is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authenticateService.authenticateUser(null, "password");
        }, "Should throw IllegalArgumentException for null username");

        assertEquals("Username cannot be null", exception.getMessage(), "Exception message should match");

        // Verify: Ensure AuthenticationManager was called, but JwtUtil was not
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Tests authentication with empty password.
     * Verifies that BadCredentialsException is thrown.
     */
    @Test
    void testAuthenticateUserEmptyPassword() {
        // Arrange: Mock AuthenticationManager to throw BadCredentialsException
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Password cannot be empty"));

        // Act & Assert: Verify that the exception is thrown
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authenticateService.authenticateUser("testuser", "");
        }, "Should throw BadCredentialsException for empty password");

        assertEquals("Password cannot be empty", exception.getMessage(), "Exception message should match");

        // Verify: Ensure AuthenticationManager was called, but JwtUtil was not
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Tests authentication when JwtUtil throws an exception during token generation.
     * Verifies that the exception is propagated.
     */
    @Test
    void testAuthenticateUserJwtGenerationFailure() {
        // Arrange: Mock AuthenticationManager and JwtUtil behavior
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenThrow(new RuntimeException("Failed to generate JWT"));

        // Act & Assert: Verify that the exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authenticateService.authenticateUser("testuser", "password");
        }, "Should throw RuntimeException for JWT generation failure");

        assertEquals("Failed to generate JWT", exception.getMessage(), "Exception message should match");

        // Verify: Ensure AuthenticationManager and JwtUtil were called
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(userDetails);
        verifyNoMoreInteractions(jwtUtil);
    }
}
