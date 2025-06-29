package com.company.employee.controllers;

import com.company.employee.configs.TestConfig;
import com.company.employee.configs.TestSecurityConfig;
import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.EmployeeRequestDTO;
import com.company.employee.dto.EmployeeResponseDTO;
import com.company.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for EmployeeOperationsController.
 * Uses @WebMvcTest to test the controller layer with mocked EmployeeService and JwtUtil.
 * Uses MockitoExtension to initialize mocks.
 * Imports TestSecurityConfig and TestConfig to apply security and provide mocked beans.
 */
@WebMvcTest(controllers = EmployeeOperationsController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                com.company.employee.security.JwtAuthenticationFilter.class,
                                com.company.employee.security.JwtUtil.class,
                                com.company.employee.security.CustomAuthenticationEntryPoint.class,
                                com.company.employee.configs.EmployeeSecurityConfig.class
                        }
                )
        },
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class,
                com.company.employee.configs.EmployeeSecurityConfig.class
        })
@ExtendWith(MockitoExtension.class)
@Import({TestSecurityConfig.class, TestConfig.class})
public class EmployeeOperationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService; // Injected from TestConfig

    @Autowired
    private ObjectMapper objectMapper; // For JSON serialization/deserialization

    private EmployeeRequestDTO sampleEmployeeRequestDTO;
    private ApiResponseDTO<List<EmployeeDTO>> samplePagedResponse;
    private ApiResponseDTO<EmployeeResponseDTO> sampleResponseDTO;
    private ArrayList<EmployeeDTO> sampleEmployeeList;

    /**
     * Setup method to initialize test data before each test.
     */
    @BeforeEach
    void setUp() {
        final EmployeeDTO sampleEmployeeDTO = getEmployeeDTO();
        sampleEmployeeList = new ArrayList<>();
        sampleEmployeeList.add(sampleEmployeeDTO);
        sampleEmployeeRequestDTO = new EmployeeRequestDTO();
        sampleEmployeeRequestDTO.setEmpDetailsList(sampleEmployeeList);
        samplePagedResponse = new ApiResponseDTO<>("success", "Fetching page 1 with 1 Employee data records", sampleEmployeeList);
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(sampleEmployeeList, null);
        sampleResponseDTO = new ApiResponseDTO<>("success", "Successfully found Employee Id 1 data records", responseDTO);
        reset(employeeService);
    }

    private static EmployeeDTO getEmployeeDTO() {
        EmployeeDTO sampleEmployeeDTO = new EmployeeDTO();
        sampleEmployeeDTO.setEmployeeId(1);
        sampleEmployeeDTO.setFirstName("John");
        sampleEmployeeDTO.setLastName("Doe");
        sampleEmployeeDTO.setDateOfBirth(new Date(631152000000L)); // 1990-01-01
        sampleEmployeeDTO.setGender("Male");
        sampleEmployeeDTO.setSalary(50000.0);
        sampleEmployeeDTO.setHireDate(new Date(1672531200000L)); // 2023-01-01
        sampleEmployeeDTO.setJobStage("L1");
        sampleEmployeeDTO.setDesignation("Software Engineer");
        sampleEmployeeDTO.setManagerEmployeeId(2);
        return sampleEmployeeDTO;
    }

    /**
     * Tests GET /api/v1/employees/testConnection
     * Verifies HTTP 200 and success message.
     * Public endpoint (no authentication).
     */
    @Test
    void testPostmanToApplicationConnection_Success() throws Exception {
        mockMvc.perform(get("/api/v1/employees/testConnection")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Connection to Employee Application is successfully established."));
    }

    /**
     * Tests GET /api/v1/employees/testDataBaseConnection
     * Mocks EmployeeService to return successful database connection.
     * Public endpoint (no authentication).
     */
    @Test
    void testDataBaseConnection_Success() throws Exception {
        ApiResponseDTO<String> dbResponse = new ApiResponseDTO<>("success", "Connection from Employee Application to Employee Database successfully established.", null);
        when(employeeService.testDatabaseConnection()).thenReturn(dbResponse);

        mockMvc.perform(get("/api/v1/employees/testDataBaseConnection")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Connection from Employee Application to Employee Database successfully established."));
    }

    /**
     * Tests GET /api/v1/employees/testDataBaseConnection for failure
     * Mocks EmployeeService to simulate database connection failure.
     * Public endpoint (no authentication).
     */
    @Test
    void testDataBaseConnection_Failure() throws Exception {
        ApiResponseDTO<String> dbResponse = new ApiResponseDTO<>("error", "Connection to Employee Database failed to be established.", null);
        when(employeeService.testDatabaseConnection()).thenReturn(dbResponse);

        mockMvc.perform(get("/api/v1/employees/testDataBaseConnection")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Connection to Employee Database failed to be established."));
    }

    /**
     * Tests GET /api/v1/employees/fetchEmployees with USER role
     * Mocks EmployeeService to return a page of employees.
     */
    @Test
    @WithMockUser(roles = "USER")
    void fetchEmployees_Success() throws Exception {
        Pageable pageable = PageRequest.of(0, 10); // Controller uses page=1 as page=0 internally
        when(employeeService.fetchPagedDataList(eq(pageable))).thenReturn(samplePagedResponse);

        MvcResult result = mockMvc.perform(get("/api/v1/employees/fetchEmployees")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Fetching page 0 with 1 Employee data records"))
                .andExpect(jsonPath("$.data[0].employeeId").value(1))
                .andExpect(jsonPath("$.data[0].firstName").value("John"))
                .andReturn();
        System.out.println("fetchEmployees_Success Response: " + result.getResponse().getContentAsString());
        verify(employeeService, times(1)).fetchPagedDataList(eq(pageable));
    }

    /**
     * Tests GET /api/v1/employees/fetchEmployees without authentication
     * Verifies HTTP 401 Unauthorized.
     */
    @Test
    void fetchEmployees_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/employees/fetchEmployees")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Tests POST /api/v1/employees/addEmployees with ADMIN role
     * Mocks EmployeeService to simulate adding employees.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void addEmployees_Success() throws Exception {
        // Setup mock response with EmployeeDTO
        ArrayList<EmployeeDTO> responseEmployeeList = new ArrayList<>();
        responseEmployeeList.add(getEmployeeDTO());
        ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
        responses.add(new ApiResponseDTO<>("success", "Successfully added Employee Id 1 data records", null));
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(responseEmployeeList, responses);
        ApiResponseDTO<EmployeeResponseDTO> apiResponse = new ApiResponseDTO<>("success", "Successfully added 1 . Add failed : 0", responseDTO);
        when(employeeService.addDataToDataBase(eq(sampleEmployeeList))).thenReturn(apiResponse);

        // Debug mock response and request serialization
        System.out.println("Mock Request JSON: " + objectMapper.writeValueAsString(sampleEmployeeRequestDTO));
        System.out.println("Mock Response JSON: " + objectMapper.writeValueAsString(apiResponse));

        MvcResult result = mockMvc.perform(post("/api/v1/employees/addEmployees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployeeRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println("addEmployees_Success Response: " + result.getResponse().getContentAsString());

        mockMvc.perform(post("/api/v1/employees/addEmployees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployeeRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Successfully added 1 . Add failed : 0"))
                .andExpect(jsonPath("$.data.empDetailsList[0].dateOfBirth").value("01-01-1990"))
                .andExpect(jsonPath("$.data.empDetailsList[0].hireDate").value("01-01-2023"));

        verify(employeeService, times(1)).addDataToDataBase(eq(sampleEmployeeList));
    }

    /**
     * Tests POST /api/v1/employees/addEmployees with USER role
     * Verifies HTTP 403 Forbidden.
     */
    @Test
    @WithMockUser(roles = "USER")
    void addEmployees_Forbidden() throws Exception {
        mockMvc.perform(post("/api/v1/employees/addEmployees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployeeRequestDTO)))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests POST /api/v1/employees/addEmployees with empty list
     * Verifies HTTP 400 for validation failure.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void addEmployees_ValidationFailure_EmptyList() throws Exception {
        EmployeeRequestDTO invalidRequest = new EmployeeRequestDTO();
        invalidRequest.setEmpDetailsList(new ArrayList<>());

        mockMvc.perform(post("/api/v1/employees/addEmployees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed: empDetailsList: Employee details list cannot be empty; "));
    }

    /**
     * Tests POST /api/v1/employees/addEmployees with invalid gender
     * Verifies HTTP 400 for validation failure.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void addEmployees_ValidationFailure_InvalidGender() throws Exception {
        EmployeeDTO invalidEmployee = new EmployeeDTO();
        invalidEmployee.setEmployeeId(1);
        invalidEmployee.setFirstName("John");
        invalidEmployee.setLastName("Doe");
        invalidEmployee.setDateOfBirth(new Date(631152000000L));
        invalidEmployee.setGender("Invalid"); // Violates @Pattern
        invalidEmployee.setSalary(50000.0);
        invalidEmployee.setHireDate(new Date(1672531200000L));
        invalidEmployee.setJobStage("L1");
        invalidEmployee.setDesignation("Software Engineer");
        invalidEmployee.setManagerEmployeeId(2);

        ArrayList<EmployeeDTO> invalidList = new ArrayList<>();
        invalidList.add(invalidEmployee);
        EmployeeRequestDTO invalidRequest = new EmployeeRequestDTO();
        invalidRequest.setEmpDetailsList(invalidList);

        mockMvc.perform(post("/api/v1/employees/addEmployees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Validation failed: empDetailsList[0].gender: Gender must be Male, Female, or Other; "));
    }

    /**
     * Tests GET /api/v1/employees/searchEmployee/{employeeId} with USER role
     * Mocks EmployeeService to return an employee.
     */
    @Test
    @WithMockUser(roles = "USER")
    void searchEmployee_Success() throws Exception {
        when(employeeService.searchDataBase(eq(1))).thenReturn(sampleResponseDTO);

        mockMvc.perform(get("/api/v1/employees/searchEmployee/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Successfully found Employee Id 1 data records"));

        verify(employeeService, times(1)).searchDataBase(eq(1));
    }

    /**
     * Tests GET /api/v1/employees/searchEmployee/{employeeId} for non-existent ID
     * Mocks EmployeeService to throw NoSuchElementException.
     */
    @Test
    @WithMockUser(roles = "USER")
    void searchEmployee_NotFound() throws Exception {
        when(employeeService.searchDataBase(eq(999))).thenThrow(new NoSuchElementException("employeeId 999 not found"));

        mockMvc.perform(get("/api/v1/employees/searchEmployee/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("Resource not found: employeeId 999 not found"));
    }

    /**
     * Tests PUT /api/v1/employees/updateEmployees with ADMIN role
     * Mocks EmployeeService to simulate updating employees.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateEmployees_Success() throws Exception {
        ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
        responses.add(new ApiResponseDTO<>("success", "Successfully updated Employee Id 1 data records", null));
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(null, responses);
        ApiResponseDTO<EmployeeResponseDTO> apiResponse = new ApiResponseDTO<>("success", "Update Success : 1 . Update Failed : 0", responseDTO);
        when(employeeService.updateDataToDataBase(eq(sampleEmployeeList))).thenReturn(apiResponse);

        mockMvc.perform(put("/api/v1/employees/updateEmployees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployeeRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Update Success : 1 . Update Failed : 0"));

        verify(employeeService, times(1)).updateDataToDataBase(eq(sampleEmployeeList));
    }

    /**
     * Tests DELETE /api/v1/employees/deleteEmployees with ADMIN role
     * Mocks EmployeeService to simulate deleting employees.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteEmployees_Success() throws Exception {
        ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
        responses.add(new ApiResponseDTO<>("success", "Successfully deleted Employee Id 1 data records", null));
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO(null, responses);
        ApiResponseDTO<EmployeeResponseDTO> apiResponse = new ApiResponseDTO<>("success", "Delete Success : 1. Delete Failed : 0", responseDTO);
        when(employeeService.deleteDataFromDataBase(eq(sampleEmployeeList))).thenReturn(apiResponse);

        mockMvc.perform(delete("/api/v1/employees/deleteEmployees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleEmployeeRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Delete Success : 1. Delete Failed : 0"));

        verify(employeeService, times(1)).deleteDataFromDataBase(eq(sampleEmployeeList));
    }
}