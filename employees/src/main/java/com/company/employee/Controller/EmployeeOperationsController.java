package com.company.employee.Controller;

import com.company.employee.DTOs.EmployeeRequestDTO;
import com.company.employee.Entity.EmployeeEntity;
import com.company.employee.Repositories.EmployeeRepository;
import com.company.employee.DTOs.EmployeeResponseDTO;
import com.company.employee.Services.EmployeeServiceImpl;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class EmployeeOperationsController {

    @Autowired
    public EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl employeeService;

    @Autowired
    private DataSource dataSource;

    // testing connection
    @GetMapping(value = "/testConnection")
    public ResponseEntity<EmployeeResponseDTO> testPostmanToApplicationConnection() {
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        erBean.setResponseMessage("Connection to Employee Application is successfully established.");
        erBean.setResponseStatusCode(HttpStatus.OK);
        return returnResponse(erBean);
    }

    // testing Database connection
    @GetMapping(value = "/testDataBaseConnection")
    public ResponseEntity<EmployeeResponseDTO> testDataBaseConnection() throws SQLException {
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if (connection.isValid(1)) {
            erBean.setResponseMessage("Database to Employee Application Connection successfully established.");
            erBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            erBean.setResponseMessage("Database Connection failed.");
            erBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(erBean);
    }

    // returning the response bean with adequate status code , message , custom header and response body information
    public ResponseEntity<EmployeeResponseDTO> returnResponse(EmployeeResponseDTO erBean) {
        return ResponseEntity
                .status(erBean.getResponseStatusCode()) // Set status code
                .header("URL", "/printEmployeeListJson")
                .body(erBean);  // Set response body
    }

    //    Fetching the database table data on request
    @GetMapping(value = "/fetchEmployees", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseDTO> fetchEmployees() {
        ArrayList<EmployeeEntity> empList = (ArrayList<EmployeeEntity>) employeeRepository.findAll();
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        if (!empList.isEmpty()) {
            erBean.setEmpDetailsList(empList);
            erBean.setResponseMessage("Fetching " + empList.size() + " employee data records");
            erBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            erBean.setResponseMessage("Given list was empty thus no data can be fetched");
            erBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(erBean);
    }

    //method to add the employee details to the database
    @PostMapping(value = "/addEmployees", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseDTO> addEmployees(@Valid @RequestBody EmployeeRequestDTO empBean) {
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        if (empBean.getEmpDetailsList() != null && !empBean.getEmpDetailsList().isEmpty()) {
            employeeService.addData(empBean.getEmpDetailsList());
            erBean.setResponseMessage("Successfully added " + empBean.getEmpDetailsList().size() + " employee data records");
            erBean.setResponseStatusCode(HttpStatus.CREATED);
        } else {
            erBean.setResponseMessage("Given list was empty thus there are no additions to the database");
            erBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(erBean);
    }

    // method to search for an employee details based on path variable {employee id}
    @GetMapping(value = "/searchEmployeeData/{employeeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseDTO> searchEmployees(@PathVariable ("employeeId") int employeeId) {
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        Optional<EmployeeEntity> index = employeeService.searchData(employeeId);
        erBean.setEmp(index.get());
            erBean.setResponseMessage("Successfully found Employee Id " + employeeId + " data records");
            erBean.setResponseStatusCode(HttpStatus.OK);
        return returnResponse(erBean);
    }

    // method to update the employee details based on employee id
    @PutMapping(value = "/updateEmployeeData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseDTO> updateEmployees(@Valid @RequestBody EmployeeRequestDTO empBean) {
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        if ((employeeService.searchData(empBean.getEmpDetailsList().get(0).getEmployeeId())).isPresent()) {
            employeeService.addData(empBean.getEmpDetailsList());
            erBean.setResponseMessage("Successfully updated Employee Id " + empBean.getEmpDetailsList().get(0).getEmployeeId() + " data records");
            erBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            erBean.setResponseMessage("Employee Id " + empBean.getEmpDetailsList().get(0).getEmployeeId() + " was not found.");
            erBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(erBean);
    }

    // method to remove the employee details from the database or local class variable
    @DeleteMapping(value = "/deleteEmployeeData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseDTO> deleteEmployees(@RequestBody EmployeeRequestDTO empBean) {
        EmployeeResponseDTO erBean = new EmployeeResponseDTO();
        if ((employeeService.searchData(empBean.getEmp().getEmployeeId()).isPresent())) {
            employeeRepository.deleteById(empBean.getEmp().getEmployeeId());
            erBean.setResponseMessage("Successfully deleted Employee Id " + empBean.getEmp().getEmployeeId() + " data records");
            erBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            erBean.setResponseMessage("Employee Id " + empBean.getEmp().getEmployeeId() + " was not found.");
            erBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(erBean);
    }
}
