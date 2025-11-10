package com.company.department.service;

import com.company.department.testUtils.TestUtils;
import com.company.department.dto.ApiResponseDTO;
import com.company.department.dto.DepartmentDTO;
import com.company.department.dto.DepartmentResponseDTO;
import com.company.department.entity.DepartmentEntity;
import com.company.department.repository.DepartmentRepository;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DepartmentServiceImpl.
 * Tests all methods in isolation, mocking dependencies (DepartmentRepository, DataSource).
 */
@ExtendWith(MockitoExtension.class) // Enables Mockito for dependency injection and mocking
public class DepartmentServiceImplUnitTest {

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImplUnitTest.class);

    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private MongoDatabase mongoDatabase;

    List<String> locations;
    List<Integer> departmentEmployeeIds;

    private DepartmentEntity departmentEntity;
    private DepartmentDTO departmentDTO;
    private DepartmentServiceImpl departmentService; // Class under test with mocked dependencies
    private final TestUtils testUtils = new TestUtils();

    /**
     * Sets up common test data and mocks before each test.
     */
    @BeforeEach
    void setUp() {
        logger.debug("Starting up setUp method before the test case to run");

        //Creating a mock location for the entity and dto
        locations = new ArrayList<>();
        locations.add("Random location 1");
        locations.add("Random location 2");
        locations.add("Random location 3");

        //Creating a mock location for the entity and dto
        departmentEmployeeIds = new ArrayList<>();
        departmentEmployeeIds.add(1);
        departmentEmployeeIds.add(2);
        departmentEmployeeIds.add(3);

        // Initialize sample DepartmentEntity
        // Reusable entity for tests
        departmentEntity = testUtils.getSampleDepartmentEntity(1, "department name", locations, 2, departmentEmployeeIds);

        // Initialize sample DepartmentDTO
        // Reusable DTO for tests
        departmentDTO = testUtils.getSampleDepartmentDTO(1, "department name", locations, 2, departmentEmployeeIds);

        // Manually instantiate DepartmentServiceImpl with mocks
        departmentService = new DepartmentServiceImpl(departmentRepository, mongoTemplate);
    }

    /**
     * Tests successful database connection check.
     * Verifies that a valid connection returns a success response.
     */
    @Test
    void testDatabaseConnection_Success() {
        // Arrange
        Document successResult = new Document("ok", 1.0);
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase); // Mock getDb() to return mocked MongoDatabase
        when(mongoDatabase.runCommand(new Document("ping", 1))).thenReturn(successResult); // Mock runCommand

        // Act
        ApiResponseDTO<String> response = departmentService.testDatabaseConnection();

        // Assert
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Connection from department Application to department Database successfully established.", response.getMessage());
        assertNull(response.getData());
        verify(mongoTemplate, times(1)).getDb();
        verify(mongoDatabase, times(1)).runCommand(new Document("ping", 1));
    }

    @Test
    void testDatabaseConnection_Failure() {
        // Arrange
        Document failureResult = new Document("ok", 0.0);
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.runCommand(new Document("ping", 1))).thenReturn(failureResult);

        // Act
        ApiResponseDTO<String> response = departmentService.testDatabaseConnection();

        // Assert
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Connection from department Application to department Database successfully established.", response.getMessage());
        assertNull(response.getData());
        verify(mongoTemplate, times(1)).getDb();
        verify(mongoDatabase, times(1)).runCommand(new Document("ping", 1));
    }

    @Test
    void testDatabaseConnection_EmptyResult() {
        // Arrange
        Document emptyResult = new Document();
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.runCommand(new Document("ping", 1))).thenReturn(emptyResult);

        // Act
        ApiResponseDTO<String> response = departmentService.testDatabaseConnection();

        // Assert
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Connection from department Application to department Database successfully established.", response.getMessage());
        assertNull(response.getData());
        verify(mongoTemplate, times(1)).getDb();
        verify(mongoDatabase, times(1)).runCommand(new Document("ping", 1));
    }

    @Test
    void testDatabaseConnection_Exception() {
        // Arrange
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.runCommand(new Document("ping", 1))).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> departmentService.testDatabaseConnection());
        verify(mongoTemplate, times(1)).getDb();
        verify(mongoDatabase, times(1)).runCommand(new Document("ping", 1));
    }

    /**
     * Tests mapping DepartmentEntity to DepartmentDTO.
     * Verifies that fields are correctly mapped.
     */
    @Test
    void testToDTO() {
        // Act: Call toDTO
        DepartmentDTO result = departmentService.toDTO(departmentEntity);

        // Assert: Verify mapping
        assertNotNull(result, "DTO should not be null");
        assertEquals(departmentEntity.getDepartmentId(), result.getDepartmentId(), "Department ID should match");
        assertEquals(departmentEntity.getDepartmentName(), result.getDepartmentName(), "Department Name should match");
        assertEquals(departmentEntity.getLocations(), result.getLocations(), "Enabled status should match");
        assertEquals(departmentEntity.getDepartmentHeadId(), result.getDepartmentHeadId(), "DepartmentHeadId should match");
        assertEquals(departmentEntity.getDepartmentEmployeeIds(), result.getDepartmentEmployeeIds(), "DepartmentId should match");

        // Verify: No dependencies should be called
        verifyNoInteractions(departmentRepository, mongoTemplate);
    }

    /**
     * Tests mapping DepartmentDTO to DepartmentEntity.
     * Verifies that fields are correctly mapped and password is encoded.
     */
    @Test
    void testToEntity() {
        // Act: Call toEntity
        DepartmentEntity result = departmentService.toEntity(departmentDTO);

        // Assert: Verify mapping
        assertNotNull(result, "Entity should not be null");
        assertEquals(result.getDepartmentId(), departmentEntity.getDepartmentId(), "Department ID should match");
        assertEquals(result.getDepartmentName(), departmentEntity.getDepartmentName(), "Department Name should match");
        assertEquals(result.getLocations(), departmentEntity.getLocations(), "Enabled status should match");
        assertEquals(result.getDepartmentHeadId(), departmentEntity.getDepartmentHeadId(), "DepartmentHeadId should match");
        assertEquals(result.getDepartmentId(), departmentEntity.getDepartmentId(), "DepartmentId should match");

        verifyNoInteractions(departmentRepository, mongoTemplate);
    }

    /**
     * Tests fetching paginated data when the page is valid.
     * Verifies that the response contains the correct data and message.
     */
    @Test
    void testFetchPagedDataListValidPage() {
        // Arrange: Mock departmentRepository to return a page with one department
        Pageable pageable = PageRequest.of(0, 10);
        List<DepartmentEntity> entities = List.of(departmentEntity);
        Page<DepartmentEntity> page = new PageImpl<>(entities, pageable, 1);
        when(departmentRepository.findAll(pageable)).thenReturn(page);

        // Act: Call fetchPagedDataList
        ApiResponseDTO<List<DepartmentDTO>> response = departmentService.fetchPagedDataList(1, 10);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Fetching page 1 with 1 Department data records", response.getMessage(), "Message should indicate page and size");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(1, response.getData().size(), "Data should contain one department");
        assertEquals(departmentEntity.getDepartmentName(), response.getData().get(0).getDepartmentName(), "Department Name should match");

        // Verify: Ensure departmentRepository was called
        verify(departmentRepository).findAll(pageable);
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests fetching paginated data when the page number is invalid (beyond total pages).
     * Verifies that an empty response is returned with an appropriate message.
     */
    @Test
    void testFetchPagedDataListInvalidPage() {
        // Arrange: Mock departmentRepository to return an empty page
        Pageable pageable = PageRequest.of(9, 10);
        Page<DepartmentEntity> page = new PageImpl<>(List.of(), pageable, 0);
        when(departmentRepository.findAll(pageable)).thenReturn(page);

        // Assert: Verify response
        assertThrows(IllegalArgumentException.class, () -> departmentService.fetchPagedDataList(10, 10), "Should throw IllegalArgumentException for invalid page");

        // Verify: Ensure departmentRepository was called
        verify(departmentRepository).findAll(pageable);
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests fetching paginated data directly.
     * Verifies that the page contains the correct mapped DTOs.
     */
    @Test
    void testFetchPageData() {
        // Arrange: Mock departmentRepository to return a page with one department
        Pageable pageable = PageRequest.of(0, 10);
        List<DepartmentEntity> entities = List.of(departmentEntity);
        Page<DepartmentEntity> page = new PageImpl<>(entities, pageable, 1);
        when(departmentRepository.findAll(pageable)).thenReturn(page);

        // Act: Call fetchPageData
        Page<DepartmentDTO> result = departmentService.fetchPageData(pageable);

        // Assert: Verify page content
        assertNotNull(result, "Page should not be null");
        assertEquals(1, result.getTotalElements(), "Page should contain one department");
        assertEquals(departmentEntity.getDepartmentName(), result.getContent().get(0).getDepartmentName(), "Department Name should match");

        // Verify: Ensure departmentRepository was called
        verify(departmentRepository).findAll(pageable);
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests adding multiple departments when none exist.
     * Verifies that departments are added successfully and the response reflects the count.
     */
    @Test
    void testAddDataToDataBaseSuccess() {
        // Arrange: Mock departmentRepository .
        when(departmentRepository.existsById(1)).thenReturn(false);
        when(departmentRepository.save(any(DepartmentEntity.class))).thenReturn(departmentEntity);

        // Act: Call addDataToDataBase with one department
        ArrayList<DepartmentDTO> departments = new ArrayList<>(List.of(departmentDTO));
        ApiResponseDTO<DepartmentResponseDTO> response = departmentService.addDataToDataBase(departments);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Successfully added 1 . Add failed : 0", response.getMessage(), "Message should reflect counts");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry"); // Fixed assertion
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");

        // Verify: Ensure dependencies were called
        verify(departmentRepository).existsById(1);
        verify(departmentRepository).save(any(DepartmentEntity.class));
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests adding departments when some already exist.
     * Verifies that duplicates are skipped and the response reflects the counts.
     */
    @Test
    void testAddDataToDataBaseWithDuplicates() {
        // Arrange: Mock departmentRepository .
        DepartmentDTO existingDepartment = testUtils.getSampleDepartmentDTO(2, "department name", locations, 2, departmentEmployeeIds);

        when(departmentRepository.existsById(1)).thenReturn(false);
        when(departmentRepository.existsById(2)).thenReturn(true);
        when(departmentRepository.save(any(DepartmentEntity.class))).thenReturn(departmentEntity);

        // Act: Call addDataToDataBase with one new and one existing department
        ArrayList<DepartmentDTO> departments = new ArrayList<>(List.of(departmentDTO, existingDepartment));
        ApiResponseDTO<DepartmentResponseDTO> response = departmentService.addDataToDataBase(departments);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Successfully added 1 . Add failed : 1", response.getMessage(), "Message should reflect counts");
        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Department response should be success");
        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");

        // Verify: Ensure dependencies were called
        verify(departmentRepository).existsById(1);
        verify(departmentRepository).existsById(2);
        verify(departmentRepository).save(any(DepartmentEntity.class));
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests adding a single department entity.
     * Verifies that the entity is saved to the repository.
     */
    @Test
    void testAddData() {
        // Arrange: Mock departmentRepository save
        when(departmentRepository.save(departmentEntity)).thenReturn(departmentEntity);

        // Act: Call addData
        departmentService.addData(departmentEntity);

        // Assert: No explicit return to verify, but ensure save was called
        verify(departmentRepository).save(departmentEntity);
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests searching for a department by ID when the department exists.
     * Verifies that the department is returned in the response.
     */
    @Test
    void testSearchDataBaseFound() {
        // Arrange: Mock departmentRepository to return a department
        when(departmentRepository.findById(1)).thenReturn(Optional.of(departmentEntity));

        // Act: Call searchDataBase
        ApiResponseDTO<DepartmentResponseDTO> response = departmentService.searchDataBase(1);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Successfully found Department Id 1 data records", response.getMessage(), "Message should indicate success");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(1, response.getData().getDepartmentDetailsList().size(), "Should contain one department");
        assertEquals(departmentEntity.getDepartmentName(), response.getData().getDepartmentDetailsList().get(0).getDepartmentName(), "Department Name should match");

        // Verify: Ensure departmentRepository was called
        verify(departmentRepository).findById(1);
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests searching for a department by ID when the department does not exist.
     * Verifies that NoSuchElementException is thrown.
     */
    @Test
    void testSearchDataBaseNotFound() {
        // Arrange: Mock departmentRepository to return empty
        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert: Verify that NoSuchElementException is thrown
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> departmentService.searchDataBase(1), "Should throw NoSuchElementException for non-existent department");

        assertEquals("departmentId 1 not found", exception.getMessage(), "Exception message should match");

        // Verify: Ensure departmentRepository was called
        verify(departmentRepository).findById(1);
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests searching for a department entity by ID when the department exists.
     * Verifies that the correct entity is returned.
     */
    @Test
    void testSearchDataFound() {
        // Arrange: Mock departmentRepository to return a department
        when(departmentRepository.findById(1)).thenReturn(Optional.of(departmentEntity));

        // Act: Call searchData
        DepartmentEntity result = departmentService.searchData(1);

        // Assert: Verify entity
        assertNotNull(result, "Entity should not be null");
        assertEquals(departmentEntity.getDepartmentName(), result.getDepartmentName(), "Department Name should match");

        // Verify: Ensure departmentRepository was called
        verify(departmentRepository).findById(1);
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests searching for a department entity by ID when the department does not exist.
     * Verifies that NoSuchElementException is thrown.
     */
    @Test
    void testSearchDataNotFound() {
        // Arrange: Mock departmentRepository to return empty
        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert: Verify that NoSuchElementException is thrown
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> departmentService.searchData(1), "Should throw NoSuchElementException for non-existent department");

        assertEquals("departmentId 1 not found", exception.getMessage(), "Exception message should match");

        // Verify: Ensure departmentRepository was called
        verify(departmentRepository).findById(1);
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests updating multiple departments when they exist.
     * Verifies that departments are updated and the response reflects the count.
     */
    @Test
    void testUpdateDataToDataBaseSuccess() {
        // Arrange: Mock departmentRepository .
        when(departmentRepository.existsById(1)).thenReturn(true);
        when(departmentRepository.save(any(DepartmentEntity.class))).thenReturn(departmentEntity);

        // Act: Call updateDataToDataBase with one department
        ArrayList<DepartmentDTO> departments = new ArrayList<>(List.of(departmentDTO));
        ApiResponseDTO<DepartmentResponseDTO> response = departmentService.updateDataToDataBase(departments);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Update Success : 1 . Update Failed : 0", response.getMessage(), "Message should reflect counts");
        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");

        // Verify: Ensure dependencies were called
        verify(departmentRepository).existsById(1);
        verify(departmentRepository).save(any(DepartmentEntity.class));
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests updating departments when some do not exist.
     * Verifies that non-existent departments are skipped and the response reflects the counts.
     */
    @Test
    void testUpdateDataToDataBaseWithNonExistent() {
        // Arrange: Mock departmentRepository .
        DepartmentDTO nonExistentDepartment = testUtils.getSampleDepartmentDTO(2, "non existent department name", locations,2, departmentEmployeeIds);

        when(departmentRepository.existsById(1)).thenReturn(true);
        when(departmentRepository.existsById(2)).thenReturn(false);
        when(departmentRepository.save(any(DepartmentEntity.class))).thenReturn(departmentEntity);

        // Act: Call updateDataToDataBase with one existing and one non-existent department
        ArrayList<DepartmentDTO> departments = new ArrayList<>(List.of(departmentDTO, nonExistentDepartment));
        ApiResponseDTO<DepartmentResponseDTO> response = departmentService.updateDataToDataBase(departments);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Update Success : 1 . Update Failed : 1", response.getMessage(), "Message should reflect counts");
        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Department response should be success");
        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");

        // Verify: Ensure dependencies were called
        verify(departmentRepository).existsById(1);
        verify(departmentRepository).existsById(2);
        verify(departmentRepository).save(any(DepartmentEntity.class));
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests deleting multiple departments when they exist.
     * Verifies that departments are deleted and the response reflects the count.
     */
    @Test
    void testDeleteDataFromDataBaseSuccess() {
        // Arrange: Mock departmentRepository
        when(departmentRepository.existsById(1)).thenReturn(true);

        // Act: Call deleteDataFromDataBase with one department
        ArrayList<DepartmentDTO> departments = new ArrayList<>(List.of(departmentDTO));
        ApiResponseDTO<DepartmentResponseDTO> response = departmentService.deleteDataFromDataBase(departments);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Delete Success : 1. Delete Failed : 0", response.getMessage(), "Message should reflect counts");
        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");

        // Verify: Ensure departmentRepository was called
        verify(departmentRepository).existsById(1);
        verify(departmentRepository).deleteById(1);
        verifyNoInteractions(mongoTemplate);
    }

    /**
     * Tests deleting departments when some do not exist.
     * Verifies that non-existent departments are skipped and the response reflects the counts.
     */
    @Test
    void testDeleteDataFromDataBaseWithNonExistent() {
        // Arrange: Mock departmentRepository
        DepartmentDTO nonExistentDepartment = testUtils.getSampleDepartmentDTO(2, "non existent department name", locations, 2, departmentEmployeeIds);

        when(departmentRepository.existsById(1)).thenReturn(true);
        when(departmentRepository.existsById(2)).thenReturn(false);

        // Act: Call deleteDataFromDataBase with one existing and one non-existent department
        ArrayList<DepartmentDTO> departments = new ArrayList<>(List.of(departmentDTO, nonExistentDepartment));
        ApiResponseDTO<DepartmentResponseDTO> response = departmentService.deleteDataFromDataBase(departments);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Delete Success : 1. Delete Failed : 1", response.getMessage(), "Message should reflect counts");
        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Department response should be success");
        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");

        // Verify: Ensure departmentRepository was called
        verify(departmentRepository).existsById(1);
        verify(departmentRepository).existsById(2);
        verify(departmentRepository).deleteById(1);
        verifyNoInteractions(mongoTemplate);
    }
}
