package com.company.authenticate.service.unitTest;

import com.company.authenticate.dto.ApiResponseDTO;
import com.company.authenticate.dto.UserProfileDTO;
import com.company.authenticate.dto.UserProfileResponseDTO;
import com.company.authenticate.entity.UserProfileEntity;
import com.company.authenticate.repository.UserRepository;
import com.company.authenticate.service.UserProfileServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Unit tests for UserProfileServiceImpl.
 * Tests all methods in isolation, mocking dependencies (UserRepository, DataSource, PasswordEncoder).
 */
@ExtendWith(MockitoExtension.class) // Enables Mockito for dependency injection and mocking
public class UserProfileServiceImplUnitTest {
    @Mock
    private UserRepository userRepository; // Mocked repository for database operations

    @Mock
    private DataSource dataSource; // Mocked data source for database connection testing

    @Mock
    private PasswordEncoder passwordEncoder; // Mocked password encoder for password hashing

    @Mock
    private Connection connection; // Mocked database connection for testDatabaseConnection

    private UserProfileServiceImpl userProfileService; // Class under test with mocked dependencies
    private UserProfileEntity userEntity; // Reusable entity for tests
    private UserProfileDTO userDTO; // Reusable DTO for tests

    /**
     * Sets up common test data and mocks before each test.
     */
    @BeforeEach
    void setUp() {
        // Initialize sample UserProfileEntity
        userEntity = new UserProfileEntity();
        userEntity.setUserId(1L);
        userEntity.setUserName("testuser");
        userEntity.setPassword("encodedPassword");
        userEntity.setRole("USER");
        userEntity.setEnabled(true);

        // Initialize sample UserProfileDTO
        userDTO = new UserProfileDTO();
        userDTO.setUserId(1L);
        userDTO.setUserName("testuser");
        userDTO.setPassword("rawPassword");
        userDTO.setRole("USER");
        userDTO.setEnabled(true);

        // Manually instantiate UserProfileServiceImpl with mocks
        userProfileService = new UserProfileServiceImpl(userRepository, dataSource, passwordEncoder);
    }

    /**
     * Tests successful database connection check.
     * Verifies that a valid connection returns a success response.
     */
    @Test
    void testTestDatabaseConnectionSuccess() throws SQLException {
        // Arrange: Mock DataSource and Connection behavior
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(true);

        // Act: Call testDatabaseConnection
        ApiResponseDTO<String> response = userProfileService.testDatabaseConnection();

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Connection from User Application to User Database successfully established.", response.getMessage(), "Message should indicate success");
        assertNull(response.getData(), "Data should be null");

        // Verify: Ensure connection was checked
        verify(dataSource).getConnection();
        verify(connection).isValid(1);
        verifyNoInteractions(userRepository, passwordEncoder);
    }

    /**
     * Tests failed database connection check.
     * Verifies that an invalid connection returns an error response.
     */
    @Test
    void testTestDatabaseConnectionFailure() throws SQLException {
        // Arrange: Mock connection to be invalid
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(false);

        // Act: Call testDatabaseConnection
        ApiResponseDTO<String> response = userProfileService.testDatabaseConnection();

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("error", response.getStatus(), "Status should be error");
        assertEquals("Connection to User Database failed to be established.", response.getMessage(), "Message should indicate failure");
        assertNull(response.getData(), "Data should be null");

        // Verify: Ensure connection was checked
        verify(connection).isValid(1);
    }

    /**
     * Tests database connection check with SQLException.
     * Verifies that an SQLException is propagated as a RuntimeException.
     */
    @Test
    void testTestDatabaseConnectionSQLException() throws SQLException {
        // Arrange: Mock connection to throw SQLException
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenThrow(new SQLException("Database error"));

        // Act & Assert: Verify that RuntimeException is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userProfileService.testDatabaseConnection();
        }, "Should throw RuntimeException for SQLException");

        assertEquals("Database error", exception.getCause().getMessage(), "Exception cause should match");

        // Verify: Ensure connection was checked
        verify(connection).isValid(1);
    }

    /**
     * Tests mapping UserProfileEntity to UserProfileDTO.
     * Verifies that fields are correctly mapped.
     */
    @Test
    void testToDTO() {
        // Act: Call toDTO
        UserProfileDTO result = userProfileService.toDTO(userEntity);

        // Assert: Verify mapping
        assertNotNull(result, "DTO should not be null");
        assertEquals(userEntity.getUserId(), result.getUserId(), "User ID should match");
        assertEquals(userEntity.getUserName(), result.getUserName(), "Username should match");
        assertEquals(userEntity.getRole(), result.getRole(), "Role should match");
        assertEquals(userEntity.isEnabled(), result.isEnabled(), "Enabled status should match");
        assertNull(result.getPassword(), "Password should not be included in DTO");

        // Verify: No dependencies should be called
        verifyNoInteractions(userRepository, dataSource, passwordEncoder);
    }

    /**
     * Tests mapping UserProfileDTO to UserProfileEntity.
     * Verifies that fields are correctly mapped and password is encoded.
     */
    @Test
    void testToEntity() {
        // Arrange: Mock password encoder
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");

        // Act: Call toEntity
        UserProfileEntity result = userProfileService.toEntity(userDTO);

        // Assert: Verify mapping
        assertNotNull(result, "Entity should not be null");
        assertEquals(userDTO.getUserId(), result.getUserId(), "User ID should match");
        assertEquals(userDTO.getUserName(), result.getUserName(), "Username should match");
        assertEquals("encodedPassword", result.getPassword(), "Password should be encoded");
        assertEquals("ROLE_"+userDTO.getRole(), result.getRole(), "Role should be ROLE_whatever_the_role_is");
        assertEquals(userDTO.isEnabled(), result.isEnabled(), "Enabled status should match");

        // Verify: Ensure password encoder was called
        verify(passwordEncoder).encode("rawPassword");
        verifyNoInteractions(userRepository, dataSource);
    }

    /**
     * Tests fetching paginated data when the page is valid.
     * Verifies that the response contains the correct data and message.
     */
    @Test
    void testFetchPagedDataListValidPage() {
        // Arrange: Mock userRepository to return a page with one user
        Pageable pageable = PageRequest.of(0, 10);
        List<UserProfileEntity> entities = List.of(userEntity);
        Page<UserProfileEntity> page = new PageImpl<>(entities, pageable, 1);
        when(userRepository.findAll(pageable)).thenReturn(page);

        // Act: Call fetchPagedDataList
        ApiResponseDTO<List<UserProfileDTO>> response = userProfileService.fetchPagedDataList(pageable);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Fetching page 1 with 1 User data records", response.getMessage(), "Message should indicate page and size");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(1, response.getData().size(), "Data should contain one user");
        assertEquals(userEntity.getUserName(), response.getData().get(0).getUserName(), "Username should match");

        // Verify: Ensure userRepository was called
        verify(userRepository).findAll(pageable);
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests fetching paginated data when the page number is invalid (beyond total pages).
     * Verifies that an empty response is returned with an appropriate message.
     */
    @Test
    void testFetchPagedDataListInvalidPage() {
        // Arrange: Mock userRepository to return an empty page
        Pageable pageable = PageRequest.of(1, 10);
        Page<UserProfileEntity> page = new PageImpl<>(List.of(), pageable, 0);
        when(userRepository.findAll(pageable)).thenReturn(page);

        // Act: Call fetchPagedDataList
        ApiResponseDTO<List<UserProfileDTO>> response = userProfileService.fetchPagedDataList(pageable);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Total number of records is lower than the current page number 2 containing 10 User data records each page.", response.getMessage(), "Message should indicate invalid page");
        assertNull(response.getData(), "Data should be null");

        // Verify: Ensure userRepository was called
        verify(userRepository).findAll(pageable);
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests fetching paginated data directly.
     * Verifies that the page contains the correct mapped DTOs.
     */
    @Test
    void testFetchPageData() {
        // Arrange: Mock userRepository to return a page with one user
        Pageable pageable = PageRequest.of(0, 10);
        List<UserProfileEntity> entities = List.of(userEntity);
        Page<UserProfileEntity> page = new PageImpl<>(entities, pageable, 1);
        when(userRepository.findAll(pageable)).thenReturn(page);

        // Act: Call fetchPageData
        Page<UserProfileDTO> result = userProfileService.fetchPageData(pageable);

        // Assert: Verify page content
        assertNotNull(result, "Page should not be null");
        assertEquals(1, result.getTotalElements(), "Page should contain one user");
        assertEquals(userEntity.getUserName(), result.getContent().get(0).getUserName(), "Username should match");

        // Verify: Ensure userRepository was called
        verify(userRepository).findAll(pageable);
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests adding multiple users when none exist.
     * Verifies that users are added successfully and the response reflects the count.
     */
    @Test
    void testAddDataToDataBaseSuccess() {
        // Arrange: Mock userRepository and passwordEncoder
        when(userRepository.existsById(1L)).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserProfileEntity.class))).thenReturn(userEntity);

        // Act: Call addDataToDataBase with one user
        ArrayList<UserProfileDTO> users = new ArrayList<>(List.of(userDTO));
        ApiResponseDTO<UserProfileResponseDTO> response = userProfileService.addDataToDataBase(users);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Successfully added 1 . Add failed : 0", response.getMessage(), "Message should reflect counts");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry"); // Fixed assertion
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");

        // Verify: Ensure dependencies were called
        verify(userRepository).existsById(1L);
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(any(UserProfileEntity.class));
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests adding users when some already exist.
     * Verifies that duplicates are skipped and the response reflects the counts.
     */
    @Test
    void testAddDataToDataBaseWithDuplicates() {
        // Arrange: Mock userRepository and passwordEncoder
        UserProfileDTO existingUser = new UserProfileDTO();
        existingUser.setUserId(2L);
        existingUser.setUserName("existinguser");
        existingUser.setPassword("rawPassword");
        existingUser.setRole("USER");
        existingUser.setEnabled(true);

        when(userRepository.existsById(1L)).thenReturn(false);
        when(userRepository.existsById(2L)).thenReturn(true);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserProfileEntity.class))).thenReturn(userEntity);

        // Act: Call addDataToDataBase with one new and one existing user
        ArrayList<UserProfileDTO> users = new ArrayList<>(List.of(userDTO, existingUser));
        ApiResponseDTO<UserProfileResponseDTO> response = userProfileService.addDataToDataBase(users);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Successfully added 1 . Add failed : 1", response.getMessage(), "Message should reflect counts");
        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "First response should be success");
        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");

        // Verify: Ensure dependencies were called
        verify(userRepository).existsById(1L);
        verify(userRepository).existsById(2L);
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(any(UserProfileEntity.class));
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests adding a single user entity.
     * Verifies that the entity is saved to the repository.
     */
    @Test
    void testAddData() {
        // Arrange: Mock userRepository save
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        // Act: Call addData
        userProfileService.addData(userEntity);

        // Assert: No explicit return to verify, but ensure save was called
        verify(userRepository).save(userEntity);
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests searching for a user by ID when the user exists.
     * Verifies that the user is returned in the response.
     */
    @Test
    void testSearchDataBaseFound() {
        // Arrange: Mock userRepository to return a user
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // Act: Call searchDataBase
        ApiResponseDTO<UserProfileResponseDTO> response = userProfileService.searchDataBase(1L);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Successfully found User Id 1 data records", response.getMessage(), "Message should indicate success");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(1, response.getData().getUserProfileList().size(), "Should contain one user");
        assertEquals(userEntity.getUserName(), response.getData().getUserProfileList().get(0).getUserName(), "Username should match");

        // Verify: Ensure userRepository was called
        verify(userRepository).findById(1L);
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests searching for a user by ID when the user does not exist.
     * Verifies that NoSuchElementException is thrown.
     */
    @Test
    void testSearchDataBaseNotFound() {
        // Arrange: Mock userRepository to return empty
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Verify that NoSuchElementException is thrown
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            userProfileService.searchDataBase(1L);
        }, "Should throw NoSuchElementException for non-existent user");

        assertEquals("userId 1 not found", exception.getMessage(), "Exception message should match");

        // Verify: Ensure userRepository was called
        verify(userRepository).findById(1L);
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests searching for a user entity by ID when the user exists.
     * Verifies that the correct entity is returned.
     */
    @Test
    void testSearchDataFound() {
        // Arrange: Mock userRepository to return a user
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // Act: Call searchData
        UserProfileEntity result = userProfileService.searchData(1L);

        // Assert: Verify entity
        assertNotNull(result, "Entity should not be null");
        assertEquals(userEntity.getUserName(), result.getUserName(), "Username should match");

        // Verify: Ensure userRepository was called
        verify(userRepository).findById(1L);
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests searching for a user entity by ID when the user does not exist.
     * Verifies that NoSuchElementException is thrown.
     */
    @Test
    void testSearchDataNotFound() {
        // Arrange: Mock userRepository to return empty
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Verify that NoSuchElementException is thrown
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            userProfileService.searchData(1L);
        }, "Should throw NoSuchElementException for non-existent user");

        assertEquals("userId 1 not found", exception.getMessage(), "Exception message should match");

        // Verify: Ensure userRepository was called
        verify(userRepository).findById(1L);
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests updating multiple users when they exist.
     * Verifies that users are updated and the response reflects the count.
     */
    @Test
    void testUpdateDataToDataBaseSuccess() {
        // Arrange: Mock userRepository and passwordEncoder
        when(userRepository.existsById(1L)).thenReturn(true);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserProfileEntity.class))).thenReturn(userEntity);

        // Act: Call updateDataToDataBase with one user
        ArrayList<UserProfileDTO> users = new ArrayList<>(List.of(userDTO));
        ApiResponseDTO<UserProfileResponseDTO> response = userProfileService.updateDataToDataBase(users);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Update Success : 1 . Update Failed : 0", response.getMessage(), "Message should reflect counts");
        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");

        // Verify: Ensure dependencies were called
        verify(userRepository).existsById(1L);
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(any(UserProfileEntity.class));
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests updating users when some do not exist.
     * Verifies that non-existent users are skipped and the response reflects the counts.
     */
    @Test
    void testUpdateDataToDataBaseWithNonExistent() {
        // Arrange: Mock userRepository and passwordEncoder
        UserProfileDTO nonExistentUser = new UserProfileDTO();
        nonExistentUser.setUserId(2L);
        nonExistentUser.setUserName("nonexistent");
        nonExistentUser.setPassword("rawPassword");
        nonExistentUser.setRole("USER");
        nonExistentUser.setEnabled(true);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserProfileEntity.class))).thenReturn(userEntity);

        // Act: Call updateDataToDataBase with one existing and one non-existent user
        ArrayList<UserProfileDTO> users = new ArrayList<>(List.of(userDTO, nonExistentUser));
        ApiResponseDTO<UserProfileResponseDTO> response = userProfileService.updateDataToDataBase(users);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Update Success : 1 . Update Failed : 1", response.getMessage(), "Message should reflect counts");
        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "First response should be success");
        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");

        // Verify: Ensure dependencies were called
        verify(userRepository).existsById(1L);
        verify(userRepository).existsById(2L);
        verify(passwordEncoder).encode("rawPassword");
        verify(userRepository).save(any(UserProfileEntity.class));
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests deleting multiple users when they exist.
     * Verifies that users are deleted and the response reflects the count.
     */
    @Test
    void testDeleteDataFromDataBaseSuccess() {
        // Arrange: Mock userRepository
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act: Call deleteDataFromDataBase with one user
        ArrayList<UserProfileDTO> users = new ArrayList<>(List.of(userDTO));
        ApiResponseDTO<UserProfileResponseDTO> response = userProfileService.deleteDataFromDataBase(users);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Delete Success : 1. Delete Failed : 0", response.getMessage(), "Message should reflect counts");
        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");

        // Verify: Ensure userRepository was called
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests deleting users when some do not exist.
     * Verifies that non-existent users are skipped and the response reflects the counts.
     */
    @Test
    void testDeleteDataFromDataBaseWithNonExistent() {
        // Arrange: Mock userRepository
        UserProfileDTO nonExistentUser = new UserProfileDTO();
        nonExistentUser.setUserId(2L);
        nonExistentUser.setUserName("nonexistent");
        nonExistentUser.setPassword("rawPassword");
        nonExistentUser.setRole("USER");
        nonExistentUser.setEnabled(true);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(false);

        // Act: Call deleteDataFromDataBase with one existing and one non-existent user
        ArrayList<UserProfileDTO> users = new ArrayList<>(List.of(userDTO, nonExistentUser));
        ApiResponseDTO<UserProfileResponseDTO> response = userProfileService.deleteDataFromDataBase(users);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Delete Success : 1. Delete Failed : 1", response.getMessage(), "Message should reflect counts");
        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "First response should be success");
        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");

        // Verify: Ensure userRepository was called
        verify(userRepository).existsById(1L);
        verify(userRepository).existsById(2L);
        verify(userRepository).deleteById(1L);
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests loading a user by username for authentication when the user exists.
     * Verifies that UserDetails is returned with correct attributes.
     */
    @Test
    void testLoadUserByUsernameFound() {
        // Arrange: Mock userRepository to return a user
        when(userRepository.findByUserName("testuser")).thenReturn(Optional.of(userEntity));

        // Act: Call loadUserByUsername
        UserDetails userDetails = userProfileService.loadUserByUsername("testuser");

        // Assert: Verify UserDetails
        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals("testuser", userDetails.getUsername(), "Username should match");
        assertEquals("encodedPassword", userDetails.getPassword(), "Password should match");
        assertEquals(1, userDetails.getAuthorities().size(), "Should have one authority");
        assertEquals("USER", userDetails.getAuthorities().iterator().next().getAuthority(), "Authority should match role");
        assertTrue(userDetails.isAccountNonExpired(), "Account should not be expired"); // Fixed to expect true
        assertTrue(userDetails.isAccountNonLocked(), "Account should not be locked"); // Fixed to expect true
        assertTrue(userDetails.isCredentialsNonExpired(), "Credentials should not be expired"); // Fixed to expect true
        assertTrue(userDetails.isEnabled(), "Account should be enabled");

        // Verify: Ensure userRepository was called
        verify(userRepository).findByUserName("testuser");
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests loading a user by username when the user does not exist.
     * Verifies that UsernameNotFoundException is thrown.
     */
    @Test
    void testLoadUserByUsernameNotFound() {
        // Arrange: Mock userRepository to return empty
        when(userRepository.findByUserName("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert: Verify that UsernameNotFoundException is thrown
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userProfileService.loadUserByUsername("nonexistent");
        }, "Should throw UsernameNotFoundException for non-existent user");

        assertEquals("User not found with username: nonexistent", exception.getMessage(), "Exception message should match");

        // Verify: Ensure userRepository was called
        verify(userRepository).findByUserName("nonexistent");
        verifyNoInteractions(dataSource, passwordEncoder);
    }

    /**
     * Tests loading a user with null username.
     * Verifies that UsernameNotFoundException is thrown.
     */
    @Test
    void testLoadUserByUsernameNull() {
        // Act & Assert: Verify that UsernameNotFoundException is thrown
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userProfileService.loadUserByUsername(null), "Should throw UsernameNotFoundException for null username");

        assertEquals("Username cannot be null or empty", exception.getMessage(), "Exception message should match");
    }
}