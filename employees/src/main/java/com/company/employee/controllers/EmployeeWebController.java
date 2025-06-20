package com.company.employee.controllers;

import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.EmployeeRequestDTO;
import com.company.employee.entity.EmployeeEntity;
import com.company.employee.service.EmployeeService;

import jakarta.persistence.EntityExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/web/employees")
public class EmployeeWebController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeWebController.class);
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeWebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private void loggingStart() {
        logger.info("\n\n\t\t********************* New Request Started ********************\n\n");
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        loggingStart();
        logger.debug("Displaying login page");
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        model.addAttribute("currentPage", "login");
        return "login";
    }

    @GetMapping("/fetchEmployees")
    @PreAuthorize("hasRole('USER')")
    public String showFetchEmployees(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        loggingStart();
        logger.debug("Displaying all employees with page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<EmployeeDTO> employeePage = employeeService.fetchPageData(pageable);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        // Sending the employees records details
        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("employeeCount", employeePage.getTotalElements());
        model.addAttribute("isAdmin", isAdmin);
        // Sending the pagination details
        paginationDetails(model, page, size, employeePage.getTotalPages());
        // Sending the current tab information
        model.addAttribute("currentPageTemplate", "fetchEmployees");
        logger.debug("Returning template: fetch-employees");
        return "fetch-employees";
    }

    public void paginationDetails(Model model, int page, int size, int totalRecords) {
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalRecords);
        model.addAttribute("pageSize", size);
    }

    @GetMapping("/addEmployees")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddEmployeeForm(Model model) {
        loggingStart();
        logger.debug("Displaying add employee form");
        model.addAttribute("employeeRequest", new EmployeeRequestDTO(new ArrayList<>(Collections.singletonList(new EmployeeDTO()))));
        model.addAttribute("currentPage", "addEmployees");
        return "add-employees";
    }

    @PostMapping("/addEmployees")
    @PreAuthorize("hasRole('ADMIN')")
    public String addEmployees(@ModelAttribute("employeeRequest") EmployeeRequestDTO employeeRequest, Model model) {
        loggingStart();
        logger.debug("Processing add employee request with input: {}", employeeRequest);
        ApiResponseDTO<String> response;
        try {
            if (!employeeRequest.getEmpDetailsList().isEmpty()) {
                EmployeeDTO dto = employeeRequest.getEmpDetailsList().get(0);
                logger.debug("Adding employee: {}", dto);
                EmployeeEntity entity = employeeService.toEntity(dto);
                employeeService.addData(entity);
                response = new ApiResponseDTO<>("success", "Employee added successfully", null);
                model.addAttribute("employeeRequest", new EmployeeRequestDTO(new ArrayList<>(Collections.singletonList(new EmployeeDTO()))));
            } else {
                response = new ApiResponseDTO<>("error", "No employee data provided", null);
            }
        } catch (EntityExistsException ex) {
            logger.error("Duplicate employee ID: {}", ex.getMessage());
            response = new ApiResponseDTO<>("error", "Employee ID already exists: " + ex.getMessage(), null);
        } catch (Exception ex) {
            logger.error("Error adding employee: {}", ex.getMessage());
            response = new ApiResponseDTO<>("error", "Error adding employee: " + ex.getMessage(), null);
        }
        logger.debug("Add response: status={}, message={}", response.getStatus(), response.getMessage());
        model.addAttribute("response", response);
        model.addAttribute("currentPage", "addEmployees");
        return "add-employees";
    }

    @GetMapping("/searchEmployees")
    @PreAuthorize("hasRole('USER')")
    public String showSearchEmployees(Model model) {
        loggingStart();
        model.addAttribute("employeeRequest", new EmployeeRequestDTO(new ArrayList<>(List.of(new EmployeeDTO()))));
        model.addAttribute("employees", new ArrayList<>());
        model.addAttribute("currentPage", "searchEmployees");
        return "search-employees";
    }

    @PostMapping("/searchEmployees")
    @PreAuthorize("hasRole('USER')")
    public String searchEmployees(@ModelAttribute("employeeRequest") EmployeeRequestDTO employeeRequest, Model model) {
        loggingStart();
        logger.debug("Processing search employee request with input: {}", employeeRequest);
        List<EmployeeDTO> employees = new ArrayList<>();
        ApiResponseDTO<String> response;

        try {
            if (!employeeRequest.getEmpDetailsList().isEmpty()) {
                EmployeeDTO dto = employeeRequest.getEmpDetailsList().get(0);
                logger.debug("Searching for employeeId: {}", dto.getEmployeeId());
                EmployeeEntity entity = employeeService.searchData(dto.getEmployeeId());
                if (entity != null) {
                    employees.add(employeeService.toDTO(entity));
                    response = new ApiResponseDTO<>("success", "Employee found", null);
                } else {
                    response = new ApiResponseDTO<>("error", "Employee ID not found", null);
                }
            } else {
                response = new ApiResponseDTO<>("error", "No employee ID provided", null);
            }
        } catch (NoSuchElementException ex) {
            logger.error("Resource not found: {}", ex.getMessage());
            response = new ApiResponseDTO<>("error", "Resource not found: " + ex.getMessage(), null);
            employees = new ArrayList<>();
        }

        logger.debug("Search response: status={}, message={}, employees={}", response.getStatus(), response.getMessage(), employees);
        model.addAttribute("employees", employees);
        model.addAttribute("response", response);
        model.addAttribute("employeeRequest", employeeRequest);
        model.addAttribute("currentPage", "searchEmployees");
        return "search-employees";
    }

    @GetMapping("/web/employees/updateEmployees")
    public String showUpdateEmployee(@RequestParam(value = "employeeId", required = true) Integer employeeId, Model model) {
        if (employeeId != null) {
            EmployeeEntity employee = employeeService.searchData(employeeId).orElseThrow(new RuntimeException("Employee not found with ID: " + employeeId));
            model.addAttribute("employee", employee);
        } else {
            model.addAttribute("employee", new EmployeeEntity()); // Default empty object for initial load
        }
        return "update-employees";
    }

    @PostMapping("/web/employees/updateEmployees")
    public String updateEmployee(@ModelAttribute EmployeeEntity employee, Model model) {
        // Existing update logic
        employeeService.addData(employee);
        model.addAttribute("response", new ApiResponseDTO<>("success", "Employee updated successfully", null));
        return "redirect:/web/employees/fetchEmployees";
    }

    @PostMapping("/updateEmployees/search")
    @PreAuthorize("hasRole('ADMIN')")
    public String searchForUpdate(@ModelAttribute("searchRequest") EmployeeRequestDTO searchRequest, Model model) {
        loggingStart();
        logger.debug("Processing update search request with input: {}", searchRequest);
        ApiResponseDTO<String> response;
        EmployeeRequestDTO employeeRequest = new EmployeeRequestDTO(new ArrayList<>(Collections.singletonList(new EmployeeDTO())));
        try {
            if (!searchRequest.getEmpDetailsList().isEmpty()) {
                EmployeeDTO dto = searchRequest.getEmpDetailsList().get(0);
                logger.debug("Searching for employeeId: {}", dto.getEmployeeId());
                EmployeeEntity entity = employeeService.searchData(dto.getEmployeeId());
                if (entity != null) {
                    employeeRequest.getEmpDetailsList().set(0, employeeService.toDTO(entity));
                    response = new ApiResponseDTO<>("success", "Employee found for update", null);
                } else {
                    response = new ApiResponseDTO<>("error", "Employee ID not found", null);
                }
            } else {
                response = new ApiResponseDTO<>("error", "No employee ID provided", null);
            }
        } catch (NoSuchElementException ex) {
            logger.error("Resource not found: {}", ex.getMessage());
            response = new ApiResponseDTO<>("error", "Resource not found: " + ex.getMessage(), null);
        }
        logger.debug("Update search response: status={}, message={}", response.getStatus(), response.getMessage());
        model.addAttribute("response", response);
        model.addAttribute("searchRequest", searchRequest);
        model.addAttribute("employeeRequest", employeeRequest);
        model.addAttribute("currentPage", "updateEmployees");
        return "update-employees";
    }

    @PostMapping("/updateEmployees")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateEmployees(@ModelAttribute("employeeRequest") EmployeeRequestDTO employeeRequest, Model model) {
        loggingStart();
        logger.debug("Processing update employee request with input: {}", employeeRequest);
        ApiResponseDTO<String> response;
        EmployeeRequestDTO searchRequest = new EmployeeRequestDTO(new ArrayList<>(Collections.singletonList(new EmployeeDTO())));
        try {
            if (!employeeRequest.getEmpDetailsList().isEmpty()) {
                EmployeeDTO dto = employeeRequest.getEmpDetailsList().get(0);
                logger.debug("EmployeeDTO: {}", dto);
                EmployeeEntity existingEntity = employeeService.searchData(dto.getEmployeeId());
                if (existingEntity != null) {
                    EmployeeEntity updatedEntity = employeeService.toEntity(dto);
                    updatedEntity.setEmployeeId(existingEntity.getEmployeeId());
                    employeeService.updateData(updatedEntity);
                    response = new ApiResponseDTO<>("success", "Employee updated successfully", null);
                } else {
                    response = new ApiResponseDTO<>("error", "Employee ID not found", null);
                }
            } else {
                response = new ApiResponseDTO<>("error", "No employee data provided", null);
            }
        } catch (NoSuchElementException ex) {
            logger.error("Resource not found: {}", ex.getMessage());
            response = new ApiResponseDTO<>("error", "Resource not found: " + ex.getMessage(), null);
        }
        logger.debug("Update response: status={}, message={}", response.getStatus(), response.getMessage());
        model.addAttribute("response", response);
        model.addAttribute("currentPage", "updateEmployees");
        model.addAttribute("employeeRequest", employeeRequest);
        model.addAttribute("searchRequest", searchRequest);
        return "update-employees";
    }

    @GetMapping("/deleteEmployees")
    @PreAuthorize("hasRole('ADMIN')")
    public String showDeleteEmployeeForm(Model model) {
        loggingStart();
        logger.debug("Redirecting deprecated /deleteEmployees to /fetchEmployees");
        return "redirect:/web/employees/fetchEmployees";
    }

    // Add pagination related details like fetchEmployee method has and it will fix the rendering error .
    @PostMapping("/batchDelete")
    @PreAuthorize("hasRole('ADMIN')")
    public String batchDeleteEmployees(@RequestParam(value = "selectedIds", required = false) List<Integer> selectedIds, Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        loggingStart();
        logger.debug("Processing batch delete request for employee IDs: {}", selectedIds);
        ApiResponseDTO<String> response;
        try {
            if (selectedIds == null || selectedIds.isEmpty()) {
                response = new ApiResponseDTO<>("error", "No employees selected for deletion", null);
            } else {
                employeeService.deleteWebAll(selectedIds);
                response = new ApiResponseDTO<>("success", "Employees deleted successfully", null);
            }
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid operation: {}", ex.getMessage());
            response = new ApiResponseDTO<>("error", ex.getMessage(), null);
        } catch (NoSuchElementException ex) {
            logger.error("Resource not found: {}", ex.getMessage());
            response = new ApiResponseDTO<>("error", "One or more employee IDs not found: " + ex.getMessage(), null);
        } catch (Exception ex) {
            logger.error("Error deleting employees: {}", ex.getMessage());
            response = new ApiResponseDTO<>("error", "Failed to delete employees: " + ex.getMessage(), null);
        }
        // Add response to model for UI feedback
        model.addAttribute("response", response);
        // Ensure page is at least 1 to avoid negative index
        int validPage = Math.max(page, 1);
        // Check total pages after deletion to adjust page if needed
        Pageable pageable = PageRequest.of(validPage - 1, size); // 0-based for Pageable
        Page<EmployeeDTO> employeePage = employeeService.fetchPageData(pageable);
        int adjustedPage = Math.min(validPage, employeePage.getTotalPages()); // 1-based page
        if (adjustedPage <= 0) {
            adjustedPage = 1; // Default to page 1 if no pages remain
        }
        // Reuse showFetchEmployees to fetch data and set model attributes
        logger.debug("Calling showFetchEmployees with adjusted page: {}, size: {}", adjustedPage, size);
        return showFetchEmployees(model, adjustedPage, size);
    }

    @GetMapping("/logout")
    public String showLogoutPage(Model model) {
        loggingStart();
        logger.debug("Displaying logout confirmation page");
        model.addAttribute("currentPage", "logout");
        return "logout";
    }
}