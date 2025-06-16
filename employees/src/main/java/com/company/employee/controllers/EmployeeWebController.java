package com.company.employee.controllers;

import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.EmployeeRequestDTO;
import com.company.employee.entity.EmployeeEntity;
import com.company.employee.repository.EmployeeRepository;
import com.company.employee.service.EmployeeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/web/employees")
public class EmployeeWebController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeWebController.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final DataSource dataSource;

    @Autowired
    public EmployeeWebController(EmployeeRepository employeeRepository, EmployeeService employeeService, DataSource dataSource) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.dataSource = dataSource;
    }

    private void loggingStart() {
        logger.info("\n\n\t\t********************* New Request Started ********************\n\n");
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        loggingStart();
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }

    @GetMapping("/fetchEmployees")
    @PreAuthorize("hasRole('USER')")
    public String showFetchEmployees(Model model) {
        loggingStart();
        logger.debug("Displaying all employees");
        model.addAttribute("employees", employeeService.fetchData());
        return "fetch-employees";
    }

    @GetMapping("/addEmployees")
    public String showAddEmployeeForm(Model model) {
        loggingStart();
        logger.debug("Displaying add employee form");
        model.addAttribute("employeeRequest", new EmployeeRequestDTO(new ArrayList<>(Collections.singletonList(new EmployeeDTO()))));
        return "add-employees";
    }

    @GetMapping("/searchEmployees")
    public String showSearchEmployeeForm(Model model) {
        loggingStart();
        logger.debug("Displaying search employee form");
        model.addAttribute("employeeRequest", new EmployeeRequestDTO(new ArrayList<>(Collections.singletonList(new EmployeeDTO()))));
        return "search-employees";
    }

    @GetMapping("/updateEmployees")
    public String showUpdateEmployeeForm(Model model) {
        loggingStart();
        logger.debug("Displaying update employee form");
        model.addAttribute("employeeRequest", new EmployeeRequestDTO(new ArrayList<>(Collections.singletonList(new EmployeeDTO()))));
        return "update-employees";
    }

    @PostMapping("/updateEmployees")
    public String updateEmployees(@ModelAttribute("employeeRequest") EmployeeRequestDTO employeeRequest, Model model) {
        loggingStart();
        logger.debug("Processing update employee request");
        if (!employeeRequest.getEmpDetailsList().isEmpty()) {
            EmployeeDTO dto = employeeRequest.getEmpDetailsList().get(0);
            EmployeeEntity entity = employeeService.searchData(dto.getEmployeeId());
            if (entity != null) {
                entity = employeeService.toEntity(dto);
                employeeService.addData(entity); // Assuming addData handles updates
                model.addAttribute("response", new ApiResponseDTO<String>("success", "Employee updated successfully",null));
            } else {
                model.addAttribute("response", new ApiResponseDTO<String>("error", "Employee ID not found",null));
            }
        } else {
            model.addAttribute("response", new ApiResponseDTO<String>("error", "No employee data provided",null));
        }
        return "update-employees";
    }

    @GetMapping("/deleteEmployees")
    public String showDeleteEmployeeForm(Model model) {
        loggingStart();
        logger.debug("Displaying delete employee form");
        model.addAttribute("employeeRequest", new EmployeeRequestDTO(new ArrayList<>(Collections.singletonList(new EmployeeDTO()))));
        return "delete-employees";
    }

    // Add logout handler
    @GetMapping("/logout")
    public String showLogoutPage(Model model) {
        loggingStart();
        logger.debug("Displaying logout confirmation page");
        return "logout";
    }
}