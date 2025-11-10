package com.java.employee_yearly_job_details.controllers;

import com.java.employee_yearly_job_details.beans.EmployeeYrJobDetailsDetailBean;
import com.java.employee_yearly_job_details.repository.EmployeesYrJobDetails;
import com.java.employee_yearly_job_details.repository.EmployeeYrJobDetailsRepository;
import com.java.employee_yearly_job_details.beans.EmployeeYrJobDetailsResponseBean;
import com.java.employee_yearly_job_details.service.EmployeeYrJobDetailsServiceImpl;


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

@RestController
public class EmployeeYrJobDetailsOperationsController {

    @Autowired
    public EmployeeYrJobDetailsRepository employeeYrJobDetailsRepository;

    @Autowired
    public EmployeeYrJobDetailsServiceImpl employeeService;

    @Autowired
    private DataSource dataSource;

    // testing connection
    @GetMapping(value = "/testEmployeeYrJobConnection")
    public ResponseEntity<EmployeeYrJobDetailsResponseBean> testPostmanToEmployeeYrJobConnection() {
        EmployeeYrJobDetailsResponseBean ejBean = new EmployeeYrJobDetailsResponseBean();
        ejBean.setResponseMessage("Connection successfully established.");
        ejBean.setResponseStatusCode(HttpStatus.OK);
        return returnResponse(ejBean);
    }

    // testing Database connection
    @GetMapping(value = "/testEmployeeYrJobDataBaseConnection")
    public ResponseEntity<EmployeeYrJobDetailsResponseBean> testEmployeeYrJobDataBaseConnection() throws SQLException {
        EmployeeYrJobDetailsResponseBean ejBean = new EmployeeYrJobDetailsResponseBean();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if (connection.isValid(1)) {
            ejBean.setResponseMessage("Database Connection successfully established.");
            ejBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            ejBean.setResponseMessage("Database Connection failed.");
            ejBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(ejBean);
    }

    // returning the response bean with adequate status code , message , custom header and response body information
    public ResponseEntity<EmployeeYrJobDetailsResponseBean> returnResponse(EmployeeYrJobDetailsResponseBean ejBean) {
        return ResponseEntity.status(ejBean.getResponseStatusCode()) // Set status code
                .header("URL", "/printEmployeeListJson").body(ejBean);  // Set response body
    }

    // Fetching the database table data on request
    @GetMapping(value = "/fetchEmployeeYrJobDetailsData", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeYrJobDetailsResponseBean> fetchEmployeeYrJobDetails() {
        ArrayList<EmployeesYrJobDetails> eyjList = (ArrayList<EmployeesYrJobDetails>) employeeYrJobDetailsRepository.findAll();
        EmployeeYrJobDetailsResponseBean ejBean = new EmployeeYrJobDetailsResponseBean();
        if (!eyjList.isEmpty()) {
            ejBean.setEyjDetailsList(eyjList);
            ejBean.setResponseMessage("SUCCESS");
            ejBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            ejBean.setResponseMessage("ERROR");
            ejBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(ejBean);
    }

    //method to add the EmployeeYrJob details to the database
    @PostMapping(value = "/addEmployeeYrJobDetailsData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeYrJobDetailsResponseBean> addEmployeeYrJobDetails(@RequestBody EmployeeYrJobDetailsDetailBean eyjBean) {
        EmployeeYrJobDetailsResponseBean ejBean = new EmployeeYrJobDetailsResponseBean();
        if (eyjBean.getEyjDetailsList() != null && !eyjBean.getEyjDetailsList().isEmpty()) {
            employeeService.addData(eyjBean.getEyjDetailsList());
            ejBean.setResponseMessage("SUCCESS");
            ejBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            ejBean.setResponseMessage("ERROR");
            ejBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(ejBean);
    }

    // method to search for an EmployeeYrJob details based on employee id, year or both .
    @GetMapping(value = "/searchEmployeeYrJobDetails", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeYrJobDetailsResponseBean> searchEmployeeYrJobDetails(@RequestBody EmployeeYrJobDetailsDetailBean eyjBean) {
        EmployeeYrJobDetailsResponseBean ejBean = new EmployeeYrJobDetailsResponseBean();
        ArrayList<EmployeesYrJobDetails> eyjList = employeeService.searchOption(eyjBean);
        if (eyjList != null && !(eyjList.isEmpty())) {
            ejBean.setEyjDetailsList(eyjList);
            ejBean.setResponseMessage("SUCCESS");
            ejBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            ejBean.setResponseMessage("ERROR");
            ejBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(ejBean);
    }

    // method to update the EmployeeYrJob details based on employee id.
    @PutMapping(value = "/updateEmployeeYrJobDetails", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeYrJobDetailsResponseBean> updateEmployeeYrJobDetails(@RequestBody EmployeeYrJobDetailsDetailBean eyjBean) {
        EmployeeYrJobDetailsResponseBean ejBean = new EmployeeYrJobDetailsResponseBean();
        if (employeeService.findAllByEmployeeID(eyjBean.getEyjDetailsList().get(0).getEmployeeID()) != null) {
            employeeService.addData(eyjBean.getEyjDetailsList());
            ejBean.setResponseMessage("SUCCESS");
            ejBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            ejBean.setResponseMessage("ERROR");
            ejBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(ejBean);
    }

    // method to remove the EmployeeYrJob details from the database or local class variable
    @DeleteMapping(value = "/deleteEmployeeYrJobDetails", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeYrJobDetailsResponseBean> deleteEmployeeYrJobDetails(@RequestBody EmployeeYrJobDetailsDetailBean eyjBean) {
        EmployeeYrJobDetailsResponseBean ejBean = new EmployeeYrJobDetailsResponseBean();
        if (employeeService.findAllByEmployeeIDAndYear(eyjBean.getEyjDetailsList().get(0).getEmployeeID() , eyjBean.getEyjDetailsList().get(0).getYear()) != null && !(employeeService.findAllByEmployeeIDAndYear(eyjBean.getEyjDetailsList().get(0).getEmployeeID() , eyjBean.getEyjDetailsList().get(0).getYear()).isEmpty())) {
            employeeService.delete(eyjBean.getEyjDetailsList().get(0).getEmployeeID(), eyjBean.getEyjDetailsList().get(0).getYear());
            ejBean.setResponseMessage("SUCCESS");
            ejBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            ejBean.setResponseMessage("ERROR");
            ejBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(ejBean);
    }
}
