//package com.company.employee.service;
//
//import com.company.employee.dto.ApiResponseDTO;
//import com.company.employee.dto.EmployeeFetchOrCreateRequest;
//import com.company.employee.dto.EmployeeResponseDTO;
//import com.company.employee.entity.EmployeeEntity;
//import com.company.employee.repository.EmployeeRepository;
//import com.company.employee.testUtils.TestUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for EmployeeServiceImpl.
// * Tests all methods in isolation, mocking dependencies (EmployeeRepository, DataSource).
// */
//@ExtendWith(MockitoExtension.class) // Enables Mockito for dependency injection and mocking
//public class EmployeeServiceImplUnitTest {
//
//    // For detailed logging in the application
//    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImplUnitTest.class);
//
//    @Mock
//    private EmployeeRepository employeeRepository;
//    @Mock
//    private DataSource dataSource;
//    @Mock
//    private Connection connection; // Mocked database connection for testDatabaseConnection
//
//    private EmployeeEntity employeeEntity;
//    private EmployeeFetchOrCreateRequest employeeFetchOrCreateRequest;
//    private EmployeeServiceImpl employeeService; // Class under test with mocked dependencies
//    private final TestUtils testUtils = new TestUtils();
//
//    /**
//     * Sets up common test data and mocks before each test.
//     */
//    @BeforeEach
//    void setUp() {
//        logger.debug("Starting up setUp method before the test case to run");
//        // Initialize sample EmployeeEntity
//        // Reusable entity for tests
//        employeeEntity = testUtils.getSampleEmployeeEntity(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        // Initialize sample EmployeeFetchOrCreateRequest
//        // Reusable DTO for tests
//        employeeFetchOrCreateRequest = testUtils.getSampleEmployeeDTO(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        // Manually instantiate EmployeeServiceImpl with mocks
//        employeeService = new EmployeeServiceImpl(employeeRepository, dataSource);
//    }
//
//    /**
//     * Tests successful database connection check.
//     * Verifies that a valid connection returns a success response.
//     */
//    @Test
//    void testTestDatabaseConnectionSuccess() throws SQLException {
//        // Arrange: Mock DataSource and Connection behavior
//        when(dataSource.getConnection()).thenReturn(connection);
//        when(connection.isValid(1)).thenReturn(true);
//
//        // Act: Call testDatabaseConnection
//        ApiResponseDTO<String> response = employeeService.testDatabaseConnection();
//
//        // Assert: Verify response
//        assertNotNull(response, "Response should not be null");
//        assertEquals("success", response.getStatus(), "Status should be success");
//        assertEquals("Connection from Employee Application to Employee Database successfully established.", response.getMessage(), "Message should indicate success");
//        assertNull(response.getData(), "Data should be null");
//
//        // Verify: Ensure connection was checked
//        verify(dataSource).getConnection();
//        verify(connection).isValid(1);
//        verifyNoInteractions(employeeRepository);
//    }
//
//    /**
//     * Tests failed database connection check.
//     * Verifies that an invalid connection returns an error response.
//     */
//    @Test
//    void testTestDatabaseConnectionFailure() throws SQLException {
//        // Arrange: Mock connection to be invalid
//        when(dataSource.getConnection()).thenReturn(connection);
//        when(connection.isValid(1)).thenReturn(false);
//        // Act: Call testDatabaseConnection
//        ApiResponseDTO<String> response = employeeService.testDatabaseConnection();
//
//        // Assert: Verify response
//        assertNotNull(response, "Response should not be null");
//        assertEquals("error", response.getStatus(), "Status should be error");
//        assertEquals("Connection to Employee Database failed to be established.", response.getMessage(), "Message should indicate failure");
//        assertNull(response.getData(), "Data should be null");
//
//        // Verify: Ensure connection was checked
//        verify(connection).isValid(1);
//    }
//
//    /**
//     * Tests database connection check with SQLException.
//     * Verifies that an SQLException is propagated as a RuntimeException.
//     */
//    @Test
//    void testTestDatabaseConnectionSQLException() throws SQLException {
//        // Arrange: Mock connection to throw SQLException
//        when(dataSource.getConnection()).thenReturn(connection);
//        when(connection.isValid(1)).thenThrow(new SQLException("Database error"));
//
//        // Act & Assert: Verify that RuntimeException is thrown
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> employeeService.testDatabaseConnection(), "Should throw RuntimeException for SQLException");
//
//        assertEquals("Database error", exception.getCause().getMessage(), "Exception cause should match");
//
//        // Verify: Ensure connection was checked
//        verify(connection).isValid(1);
//    }
//
//    /**
//     * Tests mapping EmployeeEntity to EmployeeFetchOrCreateRequest.
//     * Verifies that fields are correctly mapped.
//     */
//    @Test
//    void testToDTO() {
//        // Act: Call toDTO
//        EmployeeFetchOrCreateRequest result = employeeService.toDTO(employeeEntity);
//
//        // Assert: Verify mapping
//        assertNotNull(result, "DTO should not be null");
//        assertEquals(employeeEntity.getEmployeeId(), result.getEmployeeId(), "Employee ID should match");
//        assertEquals(employeeEntity.getFirstName(), result.getFirstName(), "First Name should match");
//        assertEquals(employeeEntity.getLastName(), result.getLastName(), "Role should match");
//        assertEquals(employeeEntity.getDateOfBirth(), result.getDateOfBirth(), "Enabled status should match");
//        assertEquals(employeeEntity.getGender(), result.getGender(), "Gender should match");
//        assertEquals(employeeEntity.getSalary(), result.getSalary(), "Salary should match");
//        assertEquals(employeeEntity.getHireDate(), result.getHireDate(), "Hiring Date should match");
//        assertEquals(employeeEntity.getJobStage(), result.getJobStage(), "Job Stage should match");
//        assertEquals(employeeEntity.getDesignation(), result.getDesignation(), "Designation should match");
//        assertEquals(employeeEntity.getManagerEmployeeId(), result.getManagerEmployeeId(), "Manager's EmployeeId should match");
//
//        // Verify: No dependencies should be called
//        verifyNoInteractions(employeeRepository, dataSource);
//    }
//
//    /**
//     * Tests mapping EmployeeFetchOrCreateRequest to EmployeeEntity.
//     * Verifies that fields are correctly mapped and password is encoded.
//     */
//    @Test
//    void testToEntity() {
//        // Act: Call toEmployeeEntity
//        EmployeeEntity result = employeeService.toEntity(employeeFetchOrCreateRequest);
//
//        // Assert: Verify mapping
//        assertNotNull(result, "Entity should not be null");
//        assertEquals(result.getEmployeeId(), employeeEntity.getEmployeeId(), "Employee ID should match");
//        assertEquals(result.getFirstName(), employeeEntity.getFirstName(), "First Name should match");
//        assertEquals(result.getLastName(), employeeEntity.getLastName(), "Role should match");
//        assertEquals(result.getDateOfBirth(), employeeEntity.getDateOfBirth(), "Enabled status should match");
//        assertEquals(result.getGender(), employeeEntity.getGender(), "Gender should match");
//        assertEquals(result.getSalary(), employeeEntity.getSalary(), "Salary should match");
//        assertEquals(result.getHireDate(), employeeEntity.getHireDate(), "Hiring Date should match");
//        assertEquals(result.getJobStage(), employeeEntity.getJobStage(), "Job Stage should match");
//        assertEquals(result.getDesignation(), employeeEntity.getDesignation(), "Designation should match");
//        assertEquals(result.getManagerEmployeeId(), employeeEntity.getManagerEmployeeId(), "Manager's EmployeeId should match");
//
//        verifyNoInteractions(employeeRepository, dataSource);
//    }
//
//    /**
//     * Tests fetching paginated data when the page is valid.
//     * Verifies that the response contains the correct data and message.
//     */
//    @Test
//    void testFetchPagedDataListValidPage() {
//        // Arrange: Mock employeeRepository to return a page with one employee
//        Pageable pageable = PageRequest.of(0, 10);
//        List<EmployeeEntity> entities = List.of(employeeEntity);
//        Page<EmployeeEntity> page = new PageImpl<>(entities, pageable, 1);
//        when(employeeRepository.findAll(pageable)).thenReturn(page);
//
//        // Act: Call fetchPagedDataList
//        ApiResponseDTO<List<EmployeeFetchOrCreateRequest>> response = employeeService.fetchPagedDataList(1, 10);
//
//        // Assert: Verify response
//        assertNotNull(response, "Response should not be null");
//        assertEquals("success", response.getStatus(), "Status should be success");
//        assertEquals("Fetching page 1 with 1 Employee data records", response.getMessage(), "Message should indicate page and size");
//        assertNotNull(response.getData(), "Data should not be null");
//        assertEquals(1, response.getData().size(), "Data should contain one employee");
//        assertEquals(employeeEntity.getFirstName(), response.getData().get(0).getFirstName(), "First Name should match");
//
//        // Verify: Ensure employeeRepository was called
//        verify(employeeRepository).findAll(pageable);
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests fetching paginated data when the page number is invalid (beyond total pages).
//     * Verifies that an empty response is returned with an appropriate message.
//     */
//    @Test
//    void testFetchPagedDataListInvalidPage() {
//        // Arrange: Mock employeeRepository to return an empty page
//        Pageable pageable = PageRequest.of(9, 10);
//        Page<EmployeeEntity> page = new PageImpl<>(List.of(), pageable, 0);
//        when(employeeRepository.findAll(pageable)).thenReturn(page);
//
//        // Assert: Verify response
//        assertThrows(IllegalArgumentException.class, () -> employeeService.fetchPagedDataList(10, 10), "Should throw IllegalArgumentException for invalid page");
//
//        // Verify: Ensure employeeRepository was called
//        verify(employeeRepository).findAll(pageable);
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests fetching paginated data directly.
//     * Verifies that the page contains the correct mapped DTOs.
//     */
//    @Test
//    void testFetchPageData() {
//        // Arrange: Mock employeeRepository to return a page with one employee
//        Pageable pageable = PageRequest.of(0, 10);
//        List<EmployeeEntity> entities = List.of(employeeEntity);
//        Page<EmployeeEntity> page = new PageImpl<>(entities, pageable, 1);
//        when(employeeRepository.findAll(pageable)).thenReturn(page);
//
//        // Act: Call fetchPageData
//        Page<EmployeeFetchOrCreateRequest> result = employeeService.fetchPageData(pageable);
//
//        // Assert: Verify page content
//        assertNotNull(result, "Page should not be null");
//        assertEquals(1, result.getTotalElements(), "Page should contain one employee");
//        assertEquals(employeeEntity.getFirstName(), result.getContent().get(0).getFirstName(), "First Name should match");
//
//        // Verify: Ensure employeeRepository was called
//        verify(employeeRepository).findAll(pageable);
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests adding multiple employees when none exist.
//     * Verifies that employees are added successfully and the response reflects the count.
//     */
//    @Test
//    void testAddDataToDataBaseSuccess() {
//        // Arrange: Mock employeeRepository .
//        when(employeeRepository.existsById(1)).thenReturn(false);
//        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
//
//        // Act: Call addDataToDataBase with one employee
//        ArrayList<EmployeeFetchOrCreateRequest> employees = new ArrayList<>(List.of(employeeFetchOrCreateRequest));
//        ApiResponseDTO<EmployeeResponseDTO> response = employeeService.addDataToDataBase(employees);
//
//        // Assert: Verify response
//        assertNotNull(response, "Response should not be null");
//        assertEquals("success", response.getStatus(), "Status should be success");
//        assertEquals("Successfully added 1 . Add failed : 0", response.getMessage(), "Message should reflect counts");
//        assertNotNull(response.getData(), "Data should not be null");
//        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry"); // Fixed assertion
//        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");
//
//        // Verify: Ensure dependencies were called
//        verify(employeeRepository).existsById(1);
//        verify(employeeRepository).save(any(EmployeeEntity.class));
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests adding employees when some already exist.
//     * Verifies that duplicates are skipped and the response reflects the counts.
//     */
//    @Test
//    void testAddDataToDataBaseWithDuplicates() {
//        // Arrange: Mock employeeRepository .
//        EmployeeFetchOrCreateRequest existingEmployee = testUtils.getSampleEmployeeDTO(2, "Sam", "White", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        when(employeeRepository.existsById(1)).thenReturn(false);
//        when(employeeRepository.existsById(2)).thenReturn(true);
//        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
//
//        // Act: Call addDataToDataBase with one new and one existing employee
//        ArrayList<EmployeeFetchOrCreateRequest> employees = new ArrayList<>(List.of(employeeFetchOrCreateRequest, existingEmployee));
//        ApiResponseDTO<EmployeeResponseDTO> response = employeeService.addDataToDataBase(employees);
//
//        // Assert: Verify response
//        assertNotNull(response, "Response should not be null");
//        assertEquals("success", response.getStatus(), "Status should be success");
//        assertEquals("Successfully added 1 . Add failed : 1", response.getMessage(), "Message should reflect counts");
//        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
//        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "First response should be success");
//        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");
//
//        // Verify: Ensure dependencies were called
//        verify(employeeRepository).existsById(1);
//        verify(employeeRepository).existsById(2);
//        verify(employeeRepository).save(any(EmployeeEntity.class));
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests adding a single employee entity.
//     * Verifies that the entity is saved to the repository.
//     */
//    @Test
//    void testAddData() {
//        // Arrange: Mock employeeRepository save
//        when(employeeRepository.save(employeeEntity)).thenReturn(employeeEntity);
//
//        // Act: Call addData
//        employeeService.addData(employeeEntity);
//
//        // Assert: No explicit return to verify, but ensure save was called
//        verify(employeeRepository).save(employeeEntity);
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests searching for an employee by ID when the employee exists.
//     * Verifies that the employee is returned in the response.
//     */
//    @Test
//    void testSearchDataBaseFound() {
//        // Arrange: Mock employeeRepository to return an employee
//        when(employeeRepository.findById(1)).thenReturn(Optional.of(employeeEntity));
//
//        // Act: Call searchDataBase
//        ApiResponseDTO<EmployeeResponseDTO> response = employeeService.searchDataBase(1);
//
//        // Assert: Verify response
//        assertNotNull(response, "Response should not be null");
//        assertEquals("success", response.getStatus(), "Status should be success");
//        assertEquals("Successfully found Employee Id 1 data records", response.getMessage(), "Message should indicate success");
//        assertNotNull(response.getData(), "Data should not be null");
//        assertEquals(1, response.getData().getEmpDetailsList().size(), "Should contain one employee");
//        assertEquals(employeeEntity.getFirstName(), response.getData().getEmpDetailsList().get(0).getFirstName(), "First Name should match");
//
//        // Verify: Ensure employeeRepository was called
//        verify(employeeRepository).findById(1);
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests searching for an employee by ID when the employee does not exist.
//     * Verifies that NoSuchElementException is thrown.
//     */
//    @Test
//    void testSearchDataBaseNotFound() {
//        // Arrange: Mock employeeRepository to return empty
//        when(employeeRepository.findById(1)).thenReturn(Optional.empty());
//
//        // Act & Assert: Verify that NoSuchElementException is thrown
//        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> employeeService.searchDataBase(1), "Should throw NoSuchElementException for non-existent employee");
//
//        assertEquals("employeeId 1 not found", exception.getMessage(), "Exception message should match");
//
//        // Verify: Ensure employeeRepository was called
//        verify(employeeRepository).findById(1);
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests searching for an employee entity by ID when the employee exists.
//     * Verifies that the correct entity is returned.
//     */
//    @Test
//    void testSearchDataFound() {
//        // Arrange: Mock employeeRepository to return an employee
//        when(employeeRepository.findById(1)).thenReturn(Optional.of(employeeEntity));
//
//        // Act: Call searchData
//        EmployeeEntity result = employeeService.searchData(1);
//
//        // Assert: Verify entity
//        assertNotNull(result, "Entity should not be null");
//        assertEquals(employeeEntity.getFirstName(), result.getFirstName(), "First Name should match");
//
//        // Verify: Ensure employeeRepository was called
//        verify(employeeRepository).findById(1);
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests searching for an employee entity by ID when the employee does not exist.
//     * Verifies that NoSuchElementException is thrown.
//     */
//    @Test
//    void testSearchDataNotFound() {
//        // Arrange: Mock employeeRepository to return empty
//        when(employeeRepository.findById(1)).thenReturn(Optional.empty());
//
//        // Act & Assert: Verify that NoSuchElementException is thrown
//        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> employeeService.searchData(1), "Should throw NoSuchElementException for non-existent employee");
//
//        assertEquals("employeeId 1 not found", exception.getMessage(), "Exception message should match");
//
//        // Verify: Ensure employeeRepository was called
//        verify(employeeRepository).findById(1);
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests updating multiple employees when they exist.
//     * Verifies that employees are updated and the response reflects the count.
//     */
//    @Test
//    void testUpdateDataToDataBaseSuccess() {
//        // Arrange: Mock employeeRepository .
//        when(employeeRepository.existsById(1)).thenReturn(true);
//        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
//
//        // Act: Call updateDataToDataBase with one employee
//        ArrayList<EmployeeFetchOrCreateRequest> employees = new ArrayList<>(List.of(employeeFetchOrCreateRequest));
//        ApiResponseDTO<EmployeeResponseDTO> response = employeeService.updateDataToDataBase(employees);
//
//        // Assert: Verify response
//        assertNotNull(response, "Response should not be null");
//        assertEquals("success", response.getStatus(), "Status should be success");
//        assertEquals("Update Success : 1 . Update Failed : 0", response.getMessage(), "Message should reflect counts");
//        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry");
//        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");
//
//        // Verify: Ensure dependencies were called
//        verify(employeeRepository).existsById(1);
//        verify(employeeRepository).save(any(EmployeeEntity.class));
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests updating employees when some do not exist.
//     * Verifies that non-existent employees are skipped and the response reflects the counts.
//     */
//    @Test
//    void testUpdateDataToDataBaseWithNonExistent() {
//        // Arrange: Mock employeeRepository .
//        EmployeeFetchOrCreateRequest nonExistentEmployee = testUtils.getSampleEmployeeDTO(2, "Sam", "White", new Date(631152000000L), "Male", 60000, new Date(1672531200000L), "L4", "Software Developer", 5);
//
//        when(employeeRepository.existsById(1)).thenReturn(true);
//        when(employeeRepository.existsById(2)).thenReturn(false);
//        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
//
//        // Act: Call updateDataToDataBase with one existing and one non-existent employee
//        ArrayList<EmployeeFetchOrCreateRequest> employees = new ArrayList<>(List.of(employeeFetchOrCreateRequest, nonExistentEmployee));
//        ApiResponseDTO<EmployeeResponseDTO> response = employeeService.updateDataToDataBase(employees);
//
//        // Assert: Verify response
//        assertNotNull(response, "Response should not be null");
//        assertEquals("success", response.getStatus(), "Status should be success");
//        assertEquals("Update Success : 1 . Update Failed : 1", response.getMessage(), "Message should reflect counts");
//        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
//        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "First response should be success");
//        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");
//
//        // Verify: Ensure dependencies were called
//        verify(employeeRepository).existsById(1);
//        verify(employeeRepository).existsById(2);
//        verify(employeeRepository).save(any(EmployeeEntity.class));
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests deleting multiple employees when they exist.
//     * Verifies that employees are deleted and the response reflects the count.
//     */
//    @Test
//    void testDeleteDataFromDataBaseSuccess() {
//        // Arrange: Mock employeeRepository
//        when(employeeRepository.existsById(1)).thenReturn(true);
//
//        // Act: Call deleteDataFromDataBase with one employee
//        ArrayList<EmployeeFetchOrCreateRequest> employees = new ArrayList<>(List.of(employeeFetchOrCreateRequest));
//        ApiResponseDTO<EmployeeResponseDTO> response = employeeService.deleteDataFromDataBase(employees);
//
//        // Assert: Verify response
//        assertNotNull(response, "Response should not be null");
//        assertEquals("success", response.getStatus(), "Status should be success");
//        assertEquals("Delete Success : 1. Delete Failed : 0", response.getMessage(), "Message should reflect counts");
//        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry");
//        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");
//
//        // Verify: Ensure employeeRepository was called
//        verify(employeeRepository).existsById(1);
//        verify(employeeRepository).deleteById(1);
//        verifyNoInteractions(dataSource);
//    }
//
//    /**
//     * Tests deleting employees when some do not exist.
//     * Verifies that non-existent employees are skipped and the response reflects the counts.
//     */
//    @Test
//    void testDeleteDataFromDataBaseWithNonExistent() {
//        // Arrange: Mock employeeRepository
//        EmployeeFetchOrCreateRequest nonExistentEmployee = testUtils.getSampleEmployeeDTO(2, "Sam", "White", new Date(631152000000L), "Male", 60000, new Date(1672531200000L), "L4", "Software Developer", 5);
//
//        when(employeeRepository.existsById(1)).thenReturn(true);
//        when(employeeRepository.existsById(2)).thenReturn(false);
//
//        // Act: Call deleteDataFromDataBase with one existing and one non-existent employee
//        ArrayList<EmployeeFetchOrCreateRequest> employees = new ArrayList<>(List.of(employeeFetchOrCreateRequest, nonExistentEmployee));
//        ApiResponseDTO<EmployeeResponseDTO> response = employeeService.deleteDataFromDataBase(employees);
//
//        // Assert: Verify response
//        assertNotNull(response, "Response should not be null");
//        assertEquals("success", response.getStatus(), "Status should be success");
//        assertEquals("Delete Success : 1. Delete Failed : 1", response.getMessage(), "Message should reflect counts");
//        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
//        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "First response should be success");
//        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");
//
//        // Verify: Ensure employeeRepository was called
//        verify(employeeRepository).existsById(1);
//        verify(employeeRepository).existsById(2);
//        verify(employeeRepository).deleteById(1);
//        verifyNoInteractions(dataSource);
//    }
//}
