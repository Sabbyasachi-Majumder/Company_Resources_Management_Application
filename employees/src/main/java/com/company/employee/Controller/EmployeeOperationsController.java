package com.company.employee.Controller;

import com.company.employee.DTOs.ApiResponseDTO;
import com.company.employee.DTOs.EmployeeRequestDTO;
import com.company.employee.DTOs.EmployeeResponseDTO;
import com.company.employee.Entity.EmployeeEntity;
import com.company.employee.Repositories.EmployeeRepository;
import com.company.employee.Services.EmployeeService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class EmployeeOperationsController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeOperationsController.class);
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final DataSource dataSource;

    // Constructor injection
    public EmployeeOperationsController(EmployeeRepository employeeRepository,
                                        EmployeeService employeeService,
                                        DataSource dataSource) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.dataSource = dataSource;
    }

    public void loggingStart() {
        logger.info("\n\n\t\t*********************New Request Started********************\n\n");
    }

    // testing connection
    @GetMapping(value = "/testConnection")
    public ResponseEntity<ApiResponseDTO<String>> testPostmanToApplicationConnection() {
        loggingStart();
        logger.info("Testing EmployeeOperationsController to Postman connection.");
        return ResponseEntity.ok(new ApiResponseDTO<>("success", "Connection to Employee Application is successfully established.", null));
    }

    // testing Database connection
    @GetMapping(value = "/testDataBaseConnection")
    public ResponseEntity<ApiResponseDTO<String>> testDataBaseConnection() throws SQLException {
        loggingStart();
        logger.info("Testing EmployeeOperationsController to employee database connection.");
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if (connection.isValid(1)) {
            logger.info("Testing successful . Database connection is present.");
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Connection to Employee Application is successfully established.", null));
        } else {
            logger.info("Testing failed . Database connection is not present.");
            return ResponseEntity.ok(new ApiResponseDTO<>("error", "Connection to Employee Database failed to be established.", null));
        }
    }

    // Fetching the database table data on request
    @GetMapping(value = "/fetchEmployees", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> fetchEmployees() {
        loggingStart();
        try {
            logger.info("Fetching all records.");
            EmployeeResponseDTO erBean = new EmployeeResponseDTO((ArrayList<EmployeeEntity>) employeeRepository.findAll(), null);
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Fetching " + erBean.getEmpDetailsList().size() + " employee data records", erBean));
        } catch (RuntimeException e) {
            logger.info("Fetching unsuccessful . Reason {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //method to add the employee details to the database
    @PostMapping(value = "/addEmployees", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> addEmployees(@Valid @RequestBody EmployeeRequestDTO empBean) {
        loggingStart();
        try {
            logger.info("Adding all records.");
            ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
            int addCounter = 0;
            for (EmployeeEntity e : empBean.getEmpDetailsList()) {
                if (!employeeRepository.existsById(e.getEmployeeId())) {
                    logger.info("Adding employeeId {} ", e.getEmployeeId());
                    employeeService.addData(e);
                    addCounter++;
                    responses.add(new ApiResponseDTO<>("success", "Successfully added Employee Id " + e.getEmployeeId() + " data records", null));
                } else {
                    logger.info("employeeId {} is already present thus not added again.", e.getEmployeeId());
                    responses.add(new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " already exists ", null));
                }
            }
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Successfully added " + addCounter + " . Add failed : " + (empBean.getEmpDetailsList().size() - addCounter), new EmployeeResponseDTO(null, responses)));

        } catch (RuntimeException e) {
            logger.info("Adding records failed . Reason {}", e.getMessage());
            return ResponseEntity.ok(new ApiResponseDTO<>("error", e.getMessage(), null));
        }
    }

    // method to search for an employee details based on path variable {employee id}
    @GetMapping(value = "/searchEmployee/{employeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> searchEmployee(@PathVariable("employeeId") int employeeId) {
        loggingStart();
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        logger.info("Searching begins");
        try {
            ArrayList<EmployeeEntity> entityArrayList = new ArrayList<>();
            entityArrayList.add(employeeService.searchData(employeeId));
            erBean.setEmpDetailsList(entityArrayList);
            logger.info("Searching employeeId {} ", employeeId);
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Successfully found Employee Id " + employeeId + " data records", erBean));
        } catch (Exception e) {
            logger.info("Search failed . Reason {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // method to update the employee details based on employee id
    @PutMapping(value = "/updateEmployees", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> updateEmployees(@Valid @RequestBody EmployeeRequestDTO empBean) {
        loggingStart();
        logger.info("Updating records begins");
        try {
            ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
            int updateCounter = 0;
            for (EmployeeEntity e : empBean.getEmpDetailsList()) {
                if (employeeRepository.existsById(e.getEmployeeId())) {
                    logger.info("Updated employeeId {} successfully", e.getEmployeeId());
                    employeeRepository.save(e);
                    updateCounter++;
                    responses.add(new ApiResponseDTO<>("success", "Successfully updated Employee Id " + e.getEmployeeId() + " data records", null));
                } else {
                    logger.info("Updating employeeId {} failed since employeeId doesn't exist", e.getEmployeeId());
                    responses.add(new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " doesn't exist", null));
                }
            }
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Update Success : " + updateCounter + " . Update Failed : " + (empBean.getEmpDetailsList().size() - updateCounter), new EmployeeResponseDTO(null, responses)));
        } catch (Exception e) {
            logger.info("Updating records failed . Reason : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // method to remove the List of employee details from the database depending on employeeId
    @DeleteMapping(value = "/deleteEmployees", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ApiResponseDTO<EmployeeResponseDTO>> deleteEmployees(@RequestBody EmployeeRequestDTO empBean) {
        loggingStart();
        logger.info("Deleting record begins");
        try {
            ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
            int deleteCounter = 0;
            for (EmployeeEntity e : empBean.getEmpDetailsList()) {
                ApiResponseDTO<EmployeeResponseDTO> apiResponse;
                if (employeeRepository.existsById(e.getEmployeeId())) {
                    logger.info("Deleted employeeId {} successfully", e.getEmployeeId());
                    employeeRepository.deleteById(e.getEmployeeId());
                    deleteCounter++;
                    apiResponse = new ApiResponseDTO<>("success", "Successfully deleted Employee Id " + e.getEmployeeId() + " data records", null);
                } else {
                    logger.info("Deleting employeeId {} failed since employeeId doesn't exist", e.getEmployeeId());
                    apiResponse = new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " doesn't exist", null);
                }
                responses.add(apiResponse);
            }
            return ResponseEntity.ok(new ApiResponseDTO<>("success", "Delete Success : " + deleteCounter + ". Delete Failed : " + (empBean.getEmpDetailsList().size() - deleteCounter), new EmployeeResponseDTO(null, responses)));
        } catch (Exception e) {
            logger.info("Deleting records failed . Reason : {} ", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
