package com.company.project.controller.service;

import com.company.project.controller.testUtils.TestUtils;
import com.company.project.dto.ApiResponseDTO;
import com.company.project.dto.ProjectDTO;
import com.company.project.dto.ProjectResponseDTO;
import com.company.project.entity.ProjectEntity;
import com.company.project.repository.ProjectRepository;
import com.company.project.service.ProjectServiceImpl;

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

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProjectServiceImpl.
 * Tests all methods in isolation, mocking dependencies (ProjectRepository, DataSource).
 */
@ExtendWith(MockitoExtension.class) // Enables Mockito for dependency injection and mocking
public class ProjectServiceImplUnitTest {

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImplUnitTest.class);

    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection; // Mocked database connection for testDatabaseConnection

    private ProjectEntity projectEntity;
    private ProjectDTO projectDTO;
    private ProjectServiceImpl projectService; // Class under test with mocked dependencies
    private final TestUtils testUtils = new TestUtils();

    /**
     * Sets up common test data and mocks before each test.
     */
    @BeforeEach
    void setUp() {
        logger.debug("Starting up setUp method before the test case to run");
        // Initialize sample ProjectEntity
        // Reusable entity for tests
        projectEntity = testUtils.getSampleProjectEntity(1, "project name", Date.valueOf("1980-04-09"), Date.valueOf("1984-04-09"), 2);

        // Initialize sample ProjectDTO
        // Reusable DTO for tests
        projectDTO = testUtils.getSampleProjectDTO(1, "project name", Date.valueOf("1980-04-09"), Date.valueOf("1984-04-09"), 2);

        // Manually instantiate ProjectServiceImpl with mocks
        projectService = new ProjectServiceImpl(projectRepository, dataSource);
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
        ApiResponseDTO<String> response = projectService.testDatabaseConnection();

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Connection from Project Application to Project Database successfully established.", response.getMessage(), "Message should indicate success");
        assertNull(response.getData(), "Data should be null");

        // Verify: Ensure connection was checked
        verify(dataSource).getConnection();
        verify(connection).isValid(1);
        verifyNoInteractions(projectRepository);
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
        ApiResponseDTO<String> response = projectService.testDatabaseConnection();

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("error", response.getStatus(), "Status should be error");
        assertEquals("Connection to Project Database failed to be established.", response.getMessage(), "Message should indicate failure");
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
        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectService.testDatabaseConnection(), "Should throw RuntimeException for SQLException");

        assertEquals("Database error", exception.getCause().getMessage(), "Exception cause should match");

        // Verify: Ensure connection was checked
        verify(connection).isValid(1);
    }

    /**
     * Tests mapping ProjectEntity to ProjectDTO.
     * Verifies that fields are correctly mapped.
     */
    @Test
    void testToDTO() {
        // Act: Call toDTO
        ProjectDTO result = projectService.toDTO(projectEntity);

        // Assert: Verify mapping
        assertNotNull(result, "DTO should not be null");
        assertEquals(projectEntity.getProjectId(), result.getProjectId(), "Project ID should match");
        assertEquals(projectEntity.getProjectName(), result.getProjectName(), "Project Name should match");
        assertEquals(projectEntity.getStartDate(), result.getStartDate(), "Enabled status should match");
        assertEquals(projectEntity.getEndDate(), result.getEndDate(), "EndDate should match");
        assertEquals(projectEntity.getDepartmentId(), result.getDepartmentId(), "DepartmentId should match");

        // Verify: No dependencies should be called
        verifyNoInteractions(projectRepository, dataSource);
    }

    /**
     * Tests mapping ProjectDTO to ProjectEntity.
     * Verifies that fields are correctly mapped and password is encoded.
     */
    @Test
    void testToEntity() {
        // Act: Call toEntity
        ProjectEntity result = projectService.toEntity(projectDTO);

        // Assert: Verify mapping
        assertNotNull(result, "Entity should not be null");
        assertEquals(result.getProjectId(), projectEntity.getProjectId(), "Project ID should match");
        assertEquals(result.getProjectName(), projectEntity.getProjectName(), "Project Name should match");
        assertEquals(result.getStartDate(), projectEntity.getStartDate(), "Enabled status should match");
        assertEquals(result.getEndDate(), projectEntity.getEndDate(), "EndDate should match");
        assertEquals(result.getDepartmentId(), projectEntity.getDepartmentId(), "DepartmentId should match");

        verifyNoInteractions(projectRepository, dataSource);
    }

    /**
     * Tests fetching paginated data when the page is valid.
     * Verifies that the response contains the correct data and message.
     */
    @Test
    void testFetchPagedDataListValidPage() {
        // Arrange: Mock projectRepository to return a page with one project
        Pageable pageable = PageRequest.of(0, 10);
        List<ProjectEntity> entities = List.of(projectEntity);
        Page<ProjectEntity> page = new PageImpl<>(entities, pageable, 1);
        when(projectRepository.findAll(pageable)).thenReturn(page);

        // Act: Call fetchPagedDataList
        ApiResponseDTO<List<ProjectDTO>> response = projectService.fetchPagedDataList(1, 10);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Fetching page 1 with 1 Project data records", response.getMessage(), "Message should indicate page and size");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(1, response.getData().size(), "Data should contain one project");
        assertEquals(projectEntity.getProjectName(), response.getData().get(0).getProjectName(), "Project Name should match");

        // Verify: Ensure projectRepository was called
        verify(projectRepository).findAll(pageable);
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests fetching paginated data when the page number is invalid (beyond total pages).
     * Verifies that an empty response is returned with an appropriate message.
     */
    @Test
    void testFetchPagedDataListInvalidPage() {
        // Arrange: Mock projectRepository to return an empty page
        Pageable pageable = PageRequest.of(9, 10);
        Page<ProjectEntity> page = new PageImpl<>(List.of(), pageable, 0);
        when(projectRepository.findAll(pageable)).thenReturn(page);

        // Assert: Verify response
        assertThrows(IllegalArgumentException.class, () -> projectService.fetchPagedDataList(10, 10), "Should throw IllegalArgumentException for invalid page");

        // Verify: Ensure projectRepository was called
        verify(projectRepository).findAll(pageable);
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests fetching paginated data directly.
     * Verifies that the page contains the correct mapped DTOs.
     */
    @Test
    void testFetchPageData() {
        // Arrange: Mock projectRepository to return a page with one project
        Pageable pageable = PageRequest.of(0, 10);
        List<ProjectEntity> entities = List.of(projectEntity);
        Page<ProjectEntity> page = new PageImpl<>(entities, pageable, 1);
        when(projectRepository.findAll(pageable)).thenReturn(page);

        // Act: Call fetchPageData
        Page<ProjectDTO> result = projectService.fetchPageData(pageable);

        // Assert: Verify page content
        assertNotNull(result, "Page should not be null");
        assertEquals(1, result.getTotalElements(), "Page should contain one project");
        assertEquals(projectEntity.getProjectName(), result.getContent().get(0).getProjectName(), "Project Name should match");

        // Verify: Ensure projectRepository was called
        verify(projectRepository).findAll(pageable);
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests adding multiple projects when none exist.
     * Verifies that projects are added successfully and the response reflects the count.
     */
    @Test
    void testAddDataToDataBaseSuccess() {
        // Arrange: Mock projectRepository .
        when(projectRepository.existsById(1)).thenReturn(false);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        // Act: Call addDataToDataBase with one project
        ArrayList<ProjectDTO> projects = new ArrayList<>(List.of(projectDTO));
        ApiResponseDTO<ProjectResponseDTO> response = projectService.addDataToDataBase(projects);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Successfully added 1 . Add failed : 0", response.getMessage(), "Message should reflect counts");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry"); // Fixed assertion
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");

        // Verify: Ensure dependencies were called
        verify(projectRepository).existsById(1);
        verify(projectRepository).save(any(ProjectEntity.class));
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests adding projects when some already exist.
     * Verifies that duplicates are skipped and the response reflects the counts.
     */
    @Test
    void testAddDataToDataBaseWithDuplicates() {
        // Arrange: Mock projectRepository .
        ProjectDTO existingProject = testUtils.getSampleProjectDTO(2, "project name", Date.valueOf("1980-04-09"), Date.valueOf("1984-04-09"), 2);

        when(projectRepository.existsById(1)).thenReturn(false);
        when(projectRepository.existsById(2)).thenReturn(true);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        // Act: Call addDataToDataBase with one new and one existing project
        ArrayList<ProjectDTO> projects = new ArrayList<>(List.of(projectDTO, existingProject));
        ApiResponseDTO<ProjectResponseDTO> response = projectService.addDataToDataBase(projects);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Successfully added 1 . Add failed : 1", response.getMessage(), "Message should reflect counts");
        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Project response should be success");
        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");

        // Verify: Ensure dependencies were called
        verify(projectRepository).existsById(1);
        verify(projectRepository).existsById(2);
        verify(projectRepository).save(any(ProjectEntity.class));
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests adding a single project entity.
     * Verifies that the entity is saved to the repository.
     */
    @Test
    void testAddData() {
        // Arrange: Mock projectRepository save
        when(projectRepository.save(projectEntity)).thenReturn(projectEntity);

        // Act: Call addData
        projectService.addData(projectEntity);

        // Assert: No explicit return to verify, but ensure save was called
        verify(projectRepository).save(projectEntity);
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests searching for a project by ID when the project exists.
     * Verifies that the project is returned in the response.
     */
    @Test
    void testSearchDataBaseFound() {
        // Arrange: Mock projectRepository to return a project
        when(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity));

        // Act: Call searchDataBase
        ApiResponseDTO<ProjectResponseDTO> response = projectService.searchDataBase(1);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Successfully found Project Id 1 data records", response.getMessage(), "Message should indicate success");
        assertNotNull(response.getData(), "Data should not be null");
        assertEquals(1, response.getData().getPrjDetailsList().size(), "Should contain one project");
        assertEquals(projectEntity.getProjectName(), response.getData().getPrjDetailsList().get(0).getProjectName(), "Project Name should match");

        // Verify: Ensure projectRepository was called
        verify(projectRepository).findById(1);
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests searching for a project by ID when the project does not exist.
     * Verifies that NoSuchElementException is thrown.
     */
    @Test
    void testSearchDataBaseNotFound() {
        // Arrange: Mock projectRepository to return empty
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert: Verify that NoSuchElementException is thrown
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> projectService.searchDataBase(1), "Should throw NoSuchElementException for non-existent project");

        assertEquals("projectId 1 not found", exception.getMessage(), "Exception message should match");

        // Verify: Ensure projectRepository was called
        verify(projectRepository).findById(1);
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests searching for a project entity by ID when the project exists.
     * Verifies that the correct entity is returned.
     */
    @Test
    void testSearchDataFound() {
        // Arrange: Mock projectRepository to return a project
        when(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity));

        // Act: Call searchData
        ProjectEntity result = projectService.searchData(1);

        // Assert: Verify entity
        assertNotNull(result, "Entity should not be null");
        assertEquals(projectEntity.getProjectName(), result.getProjectName(), "Project Name should match");

        // Verify: Ensure projectRepository was called
        verify(projectRepository).findById(1);
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests searching for a project entity by ID when the project does not exist.
     * Verifies that NoSuchElementException is thrown.
     */
    @Test
    void testSearchDataNotFound() {
        // Arrange: Mock projectRepository to return empty
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert: Verify that NoSuchElementException is thrown
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> projectService.searchData(1), "Should throw NoSuchElementException for non-existent project");

        assertEquals("projectId 1 not found", exception.getMessage(), "Exception message should match");

        // Verify: Ensure projectRepository was called
        verify(projectRepository).findById(1);
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests updating multiple projects when they exist.
     * Verifies that projects are updated and the response reflects the count.
     */
    @Test
    void testUpdateDataToDataBaseSuccess() {
        // Arrange: Mock projectRepository .
        when(projectRepository.existsById(1)).thenReturn(true);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        // Act: Call updateDataToDataBase with one project
        ArrayList<ProjectDTO> projects = new ArrayList<>(List.of(projectDTO));
        ApiResponseDTO<ProjectResponseDTO> response = projectService.updateDataToDataBase(projects);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Update Success : 1 . Update Failed : 0", response.getMessage(), "Message should reflect counts");
        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");

        // Verify: Ensure dependencies were called
        verify(projectRepository).existsById(1);
        verify(projectRepository).save(any(ProjectEntity.class));
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests updating projects when some do not exist.
     * Verifies that non-existent projects are skipped and the response reflects the counts.
     */
    @Test
    void testUpdateDataToDataBaseWithNonExistent() {
        // Arrange: Mock projectRepository .
        ProjectDTO nonExistentProject = testUtils.getSampleProjectDTO(2, "non existent project name", Date.valueOf("1980-04-09"), Date.valueOf("1980-04-09"), 2);

        when(projectRepository.existsById(1)).thenReturn(true);
        when(projectRepository.existsById(2)).thenReturn(false);
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        // Act: Call updateDataToDataBase with one existing and one non-existent project
        ArrayList<ProjectDTO> projects = new ArrayList<>(List.of(projectDTO, nonExistentProject));
        ApiResponseDTO<ProjectResponseDTO> response = projectService.updateDataToDataBase(projects);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Update Success : 1 . Update Failed : 1", response.getMessage(), "Message should reflect counts");
        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Project response should be success");
        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");

        // Verify: Ensure dependencies were called
        verify(projectRepository).existsById(1);
        verify(projectRepository).existsById(2);
        verify(projectRepository).save(any(ProjectEntity.class));
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests deleting multiple projects when they exist.
     * Verifies that projects are deleted and the response reflects the count.
     */
    @Test
    void testDeleteDataFromDataBaseSuccess() {
        // Arrange: Mock projectRepository
        when(projectRepository.existsById(1)).thenReturn(true);

        // Act: Call deleteDataFromDataBase with one project
        ArrayList<ProjectDTO> projects = new ArrayList<>(List.of(projectDTO));
        ApiResponseDTO<ProjectResponseDTO> response = projectService.deleteDataFromDataBase(projects);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Delete Success : 1. Delete Failed : 0", response.getMessage(), "Message should reflect counts");
        assertEquals(1, response.getData().getApiResponse().size(), "Response list should contain one entry");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Individual response should be success");

        // Verify: Ensure projectRepository was called
        verify(projectRepository).existsById(1);
        verify(projectRepository).deleteById(1);
        verifyNoInteractions(dataSource);
    }

    /**
     * Tests deleting projects when some do not exist.
     * Verifies that non-existent projects are skipped and the response reflects the counts.
     */
    @Test
    void testDeleteDataFromDataBaseWithNonExistent() {
        // Arrange: Mock projectRepository
        ProjectDTO nonExistentProject = testUtils.getSampleProjectDTO(2, "non existent project name", Date.valueOf("1980-04-09"), Date.valueOf("1980-04-09"), 2);

        when(projectRepository.existsById(1)).thenReturn(true);
        when(projectRepository.existsById(2)).thenReturn(false);

        // Act: Call deleteDataFromDataBase with one existing and one non-existent project
        ArrayList<ProjectDTO> projects = new ArrayList<>(List.of(projectDTO, nonExistentProject));
        ApiResponseDTO<ProjectResponseDTO> response = projectService.deleteDataFromDataBase(projects);

        // Assert: Verify response
        assertNotNull(response, "Response should not be null");
        assertEquals("success", response.getStatus(), "Status should be success");
        assertEquals("Delete Success : 1. Delete Failed : 1", response.getMessage(), "Message should reflect counts");
        assertEquals(2, response.getData().getApiResponse().size(), "Response list should contain two entries");
        assertEquals("success", response.getData().getApiResponse().get(0).getStatus(), "Project response should be success");
        assertEquals("error", response.getData().getApiResponse().get(1).getStatus(), "Second response should be error");

        // Verify: Ensure projectRepository was called
        verify(projectRepository).existsById(1);
        verify(projectRepository).existsById(2);
        verify(projectRepository).deleteById(1);
        verifyNoInteractions(dataSource);
    }
}
