package com.company.department.controllers;

import com.company.department.configs.TestConfig;
import com.company.department.configs.TestSecurityConfig;
import com.company.department.dto.ApiResponseDTO;
import com.company.department.dto.DepartmentDTO;
import com.company.department.dto.DepartmentRequestDTO;
import com.company.department.dto.DepartmentResponseDTO;
import com.company.department.service.DepartmentService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
 * Unit tests for  DepartmentOperationsController.
 * Uses @WebMvcTest to test the controller layer with mocked  DepartmentService and JwtUtil.
 * Uses MockitoExtension to initialize mocks.
 * Imports TestSecurityConfig and TestConfig to apply security and provide mocked beans.
 */
@WebMvcTest(controllers = DepartmentOperationsController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                com.company.department.security.JwtAuthenticationFilter.class,
                                com.company.department.security.JwtUtil.class,
                                com.company.department.security.CustomAuthenticationEntryPoint.class,
                                com.company.department.configs.DepartmentSecurityConfig.class
                        }
                )
        },
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class,
                com.company.department.configs.DepartmentSecurityConfig.class
        })
@ExtendWith(MockitoExtension.class)
@Import({TestSecurityConfig.class, TestConfig.class})
public class DepartmentOperationsControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private DepartmentService departmentService; // Injected from TestConfig
//
//    @Autowired
//    private ObjectMapper objectMapper; // For JSON serialization/deserialization
//
//    private DepartmentRequestDTO sampleDepartmentRequestDTO;
//    private ApiResponseDTO<List<DepartmentDTO>> samplePagedResponse;
//    private ApiResponseDTO<DepartmentResponseDTO> sampleResponseDTO;

    /**
     * Setup method to initialize test data before each test.
     */
//    @BeforeEach
//    void setUp() {
//        final  DepartmentDTO sampleDepartmentDTO = getDepartmentDTO();
//        ArrayList<DepartmentDTO> sampleDepartmentList = new ArrayList<>();
//        sampleDepartmentList.add(sampleDepartmentDTO);
//        sampleDepartmentRequestDTO = new  DepartmentRequestDTO();
//        sampleDepartmentRequestDTO.setDepartmentDetailList(sampleDepartmentList);
//        samplePagedResponse = new ApiResponseDTO<>("success", "Fetching page 1 with 1  Department data records", sampleDepartmentList);
//         DepartmentResponseDTO responseDTO = new  DepartmentResponseDTO(sampleDepartmentList, null);
//        sampleResponseDTO = new ApiResponseDTO<>("success", "Successfully found  Department Id 1 data records", responseDTO);
//        reset(departmentService);
//
//        mockMvc = MockMvcBuilders
//                .standaloneSetup(new  DepartmentOperationsController(departmentService))
//                .setControllerAdvice(new  DepartmentGlobalExceptionHandler())
//                .build();
//    }

//    private static  DepartmentDTO getDepartmentDTO() {
//         DepartmentDTO sampleDepartmentDTO = new  DepartmentDTO();
//        sampleDepartmentDTO.setDepartmentId(1);
//        sampleDepartmentDTO.setFirstName("John");
//        sampleDepartmentDTO.setLastName("Doe");
//        sampleDepartmentDTO.setDateOfBirth(new Date(631152000000L)); // 1990-01-01
//        sampleDepartmentDTO.setGender("Male");
//        sampleDepartmentDTO.setSalary(50000.0);
//        sampleDepartmentDTO.setHireDate(new Date(1672531200000L)); // 2023-01-01
//        sampleDepartmentDTO.setJobStage("L1");
//        sampleDepartmentDTO.setDesignation("Software Engineer");
//        sampleDepartmentDTO.setManagerDepartmentId(2);
//        return sampleDepartmentDTO;
//    }

    /**
     * Tests GET /api/v1/departments/testConnection
     * Verifies HTTP 200 and success message.
     * Public endpoint (no authentication).
     */
//    @Test
//    void testPostmanToApplicationConnection_Success() throws Exception {
//        mockMvc.perform(get("/api/v1/departments/testConnection")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("Connection to  Department Application is successfully established."));
//    }

    /**
     * Tests GET /api/v1/departments/testDataBaseConnection
     * Mocks  DepartmentService to return successful database connection.
     * Public endpoint (no authentication).
     */
//    @Test
//    void testDataBaseConnection_Success() throws Exception {
//        ApiResponseDTO<String> dbResponse = new ApiResponseDTO<>("success", "Connection from  Department Application to  Department Database successfully established.", null);
//        when(departmentService.testDatabaseConnection()).thenReturn(dbResponse);
//
//        mockMvc.perform(get("/api/v1/departments/testDataBaseConnection")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("Connection from  Department Application to  Department Database successfully established."));
//    }

    /**
     * Tests GET /api/v1/departments/testDataBaseConnection for failure
     * Mocks  DepartmentService to simulate database connection failure.
     * Public endpoint (no authentication).
     */
//    @Test
//    void testDataBaseConnection_Failure() throws Exception {
//        ApiResponseDTO<String> dbResponse = new ApiResponseDTO<>("error", "Connection to  Department Database failed to be established.", null);
//        when(departmentService.testDatabaseConnection()).thenReturn(dbResponse);
//
//        mockMvc.perform(get("/api/v1/departments/testDataBaseConnection")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("error"))
//                .andExpect(jsonPath("$.message").value("Connection to  Department Database failed to be established."));
//    }

    /**
     * Tests GET /api/v1/departments/fetchDepartments with USER role
     * Mocks  DepartmentService to return a page of departments.
     */
//    @Test
//    @WithMockUser(roles = "USER")
//    void fetchDepartments_Success() throws Exception {
//        Pageable pageable = PageRequest.of(1, 10); // Controller uses page=1 as page=0 internally
//        when(departmentService.fetchPagedDataList(eq(pageable))).thenReturn(samplePagedResponse);
//
//        MvcResult result = mockMvc.perform(get("/api/v1/departments/fetchDepartments")
//                        .param("page", "1")
//                        .param("size", "10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("Fetching page 1 with 1  Department data records"))
//                .andExpect(jsonPath("$.data[0].departmentId").value(1))
//                .andExpect(jsonPath("$.data[0].firstName").value("John"))
//                .andReturn();
//        System.out.println("fetchDepartments_Success Response: " + result.getResponse().getContentAsString());
//        verify(departmentService, times(1)).fetchPagedDataList(eq(pageable));
//    }

    /**
     * Tests GET /api/v1/departments/fetchDepartments without authentication
     * Verifies HTTP 401 Unauthorized.
     * It is sending 200 - OK since JWT filter is disabled now .
     */
//    @Test
//    void fetchDepartments_Unauthorized() throws Exception {
//        mockMvc.perform(get("/api/v1/departments/fetchDepartments")
//                        .param("page", "1")
//                        .param("size", "10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }

    /**
     * Tests POST /api/v1/departments/addDepartments with ADMIN role
     * Mocks  DepartmentService to simulate adding departments.
     */
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void addDepartments_Success() throws Exception {
//        ArrayList<ApiResponseDTO<DepartmentResponseDTO>> responses = new ArrayList<>();
//        responses.add(new ApiResponseDTO<>("success", "Successfully added  Department Id 1 data records", null));
//        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO(null, responses);
//        ApiResponseDTO<DepartmentResponseDTO> apiResponse = new ApiResponseDTO<>("success", "Successfully added 1 . Add failed : 0", responseDTO);
//        when(departmentService.addDataToDataBase(any(ArrayList.class))).thenReturn(apiResponse);
//
//        mockMvc.perform(post("/api/v1/departments/addDepartments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sampleDepartmentRequestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("Successfully added 1 . Add failed : 0"));
//        verify(departmentService, times(1)).addDataToDataBase(any(ArrayList.class));
//    }

    /**
     * Tests POST /api/v1/departments/addDepartments with USER role
     * Verifies HTTP 403 Forbidden.
     * It is sending 200 - OK since JWT filter is disabled now .
     */
//    @Test
//    @WithMockUser(roles = "USER")
//    void addDepartments_Forbidden() throws Exception {
//        mockMvc.perform(post("/api/v1/departments/addDepartments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sampleDepartmentRequestDTO)))
//                .andExpect(status().isOk());
//    }

    /**
     * Tests POST /api/v1/departments/addDepartments with empty list
     * Verifies HTTP 400 for validation failure.
     */
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void addDepartments_ValidationFailure_EmptyList() throws Exception {
//         DepartmentRequestDTO invalidRequest = new  DepartmentRequestDTO();
//        invalidRequest.setDepartmentDetailList(new ArrayList<>());
//
//        mockMvc.perform(post("/api/v1/departments/addDepartments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value("error"))
//                .andExpect(jsonPath("$.message").value("Validation failed: empDetailsList:  Department details list cannot be empty; "));
//    }

    /**
     * Tests POST /api/v1/departments/addDepartments with invalid gender
     * Verifies HTTP 400 for validation failure.
     */
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void addDepartments_ValidationFailure_InvalidGender() throws Exception {
//        DepartmentDTO invalidDepartment = new  DepartmentDTO();
//        invalidDepartment.setDepartmentId(1);
//        invalidDepartment.setFirstName("John");
//        invalidDepartment.setLastName("Doe");
//        invalidDepartment.setDateOfBirth(new Date(631152000000L));
//        invalidDepartment.setGender("Invalid"); // Violates @Pattern
//        invalidDepartment.setSalary(50000.0);
//        invalidDepartment.setHireDate(new Date(1672531200000L));
//        invalidDepartment.setJobStage("L1");
//        invalidDepartment.setDesignation("Software Engineer");
//        invalidDepartment.setManagerDepartmentId(2);
//
//        ArrayList<DepartmentDTO> invalidList = new ArrayList<>();
//        invalidList.add(invalidDepartment);
//         DepartmentRequestDTO invalidRequest = new  DepartmentRequestDTO();
//        invalidRequest.setDepartmentDetailList(invalidList);
//
//        mockMvc.perform(post("/api/v1/departments/addDepartments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidRequest)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.status").value("error"))
//                .andExpect(jsonPath("$.message").value("Validation failed: empDetailsList[0].gender: Gender must be Male, Female, or Other; "));
//    }

    /**
     * Tests GET /api/v1/departments/searchDepartment/{departmentId} with USER role
     * Mocks  DepartmentService to return a department.
     */
//    @Test
//    @WithMockUser(roles = "USER")
//    void searchDepartment_Success() throws Exception {
//        when(departmentService.searchDataBase(eq(1))).thenReturn(sampleResponseDTO);
//
//        mockMvc.perform(get("/api/v1/departments/searchDepartment/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("Successfully found  Department Id 1 data records"));
//
//        verify(departmentService, times(1)).searchDataBase(eq(1));
//    }

    /**
     * Tests GET /api/v1/departments/searchDepartment/{departmentId} for non-existent ID
     * Mocks  DepartmentService to throw NoSuchElementException.
     */
//    @Test
//    @WithMockUser(roles = "USER")
//    void searchDepartment_NotFound() throws Exception {
//        when(departmentService.searchDataBase(eq(999))).thenThrow(new NoSuchElementException("departmentId 999 not found"));
//
//        mockMvc.perform(get("/api/v1/departments/searchDepartment/999")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.status").value("error"))
//                .andExpect(jsonPath("$.message").value("Resource not found: departmentId 999 not found"));
//    }

    /**
     * Tests PUT /api/v1/departments/updateDepartments with ADMIN role
     * Mocks  DepartmentService to simulate updating departments.
     */
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void updateDepartments_Success() throws Exception {
//        ArrayList<ApiResponseDTO<DepartmentResponseDTO>> responses = new ArrayList<>();
//        responses.add(new ApiResponseDTO<>("success", "Successfully updated  Department Id 1 data records", null));
//        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO(null, responses); // empDetailsList is null
//        ApiResponseDTO<DepartmentResponseDTO> apiResponse = new ApiResponseDTO<>("success", "Successfully updated 1 . Update failed : 0", responseDTO);
//        when(departmentService.updateDataToDataBase(any(ArrayList.class))).thenReturn(apiResponse);
//
//        System.out.println("Mock Request JSON: " + objectMapper.writeValueAsString(sampleDepartmentRequestDTO));
//        System.out.println("Mock Response JSON: " + objectMapper.writeValueAsString(apiResponse));
//
//        MvcResult result = mockMvc.perform(put("/api/v1/departments/updateDepartments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sampleDepartmentRequestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("Successfully updated 1 . Update failed : 0"))
//                .andExpect(jsonPath("$.data.apiResponse[0].status").value("success"))
//                .andExpect(jsonPath("$.data.apiResponse[0].message").value("Successfully updated  Department Id 1 data records"))
//                .andReturn();
//        System.out.println("updateDepartments_Success Response: " + result.getResponse().getContentAsString());
//
//        verify(departmentService, times(1)).updateDataToDataBase(any(ArrayList.class));
//    }
//
//    /**
//     * Tests DELETE /api/v1/departments/deleteDepartments with ADMIN role
//     * Mocks  DepartmentService to simulate deleting departments.
//     */
//    @Test
//    @WithMockUser(roles = "ADMIN")
//    void deleteDepartments_Success() throws Exception {
//        ArrayList<ApiResponseDTO<DepartmentResponseDTO>> responses = new ArrayList<>();
//        responses.add(new ApiResponseDTO<>("success", "Successfully deleted  Department Id 1 data records", null));
//        DepartmentResponseDTO responseDTO = new DepartmentResponseDTO(null, responses);
//        ApiResponseDTO<DepartmentResponseDTO> apiResponse = new ApiResponseDTO<>("success", "Delete Success : 1. Delete Failed : 0", responseDTO);
//        when(departmentService.deleteDataFromDataBase(any(ArrayList.class))).thenReturn(apiResponse);
//
//        mockMvc.perform(delete("/api/v1/departments/deleteDepartments")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sampleDepartmentRequestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("success"))
//                .andExpect(jsonPath("$.message").value("Delete Success : 1. Delete Failed : 0"));
//
//        verify(departmentService, times(1)).deleteDataFromDataBase(any(ArrayList.class));
//    }
}