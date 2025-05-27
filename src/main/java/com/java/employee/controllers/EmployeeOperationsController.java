package com.java.employee.controllers;

import com.java.employee.beans.EmployeeDetailBean;
import com.java.employee.repository.EmployeeDetailData;
import com.java.employee.repository.EmployeeRepository;
import com.java.employee.response.EmployeeResponseBean;
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
    private DataSource dataSource;

    // testing connection
    @GetMapping(value = "/testConnection")
    public ResponseEntity<EmployeeResponseBean> testPostmanToApplicationConnection() {
        EmployeeResponseBean erBean = new EmployeeResponseBean();
        erBean.setResponseMessage("Connection successfully established.");
        erBean.setResponseStatusCode(HttpStatus.OK);
        return returnResponse(erBean);
    }

    // testing Database connection
    @GetMapping(value = "/testDataBaseConnection")
    public ResponseEntity<EmployeeResponseBean> testDataBaseConnection() throws SQLException {
        EmployeeResponseBean erBean = new EmployeeResponseBean();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if (connection.isValid(1)) {
            erBean.setResponseMessage("Database Connection successfully established.");
            erBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            erBean.setResponseMessage("Database Connection failed.");
            erBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(erBean);
    }

    // returning the response bean with key and value set
    public ResponseEntity<EmployeeResponseBean> returnResponse(EmployeeResponseBean erBean) {
        return ResponseEntity
                .status(erBean.getResponseStatusCode()) // Set status code
                .header("URL", "/printEmployeeListJson")
                .body(erBean);  // Set response body
    }

    //    Saving the data
    public void addData(ArrayList<EmployeeDetailData> empList) {
        for (EmployeeDetailData e : empList) {
            employeeRepository.save(e);
        }
    }

    //    Searching for data using empId
    public Optional<EmployeeDetailData> searchData(int empId) {
        return employeeRepository.findById(empId);
    }

    //    Fetching the database table data on request
    @GetMapping(value = "/fetchEmployeeDetailData", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseBean> fetchEmployeeDetailData() {
        ArrayList<EmployeeDetailData> empList = (ArrayList<EmployeeDetailData>) employeeRepository.findAll();
        EmployeeResponseBean erBean = new EmployeeResponseBean();
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
    @PostMapping(value = "/addEmployeeDetailData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseBean> addEmployeeDetailData(@RequestBody EmployeeDetailBean empBean) {
        EmployeeResponseBean erBean = new EmployeeResponseBean();
        if (empBean.getEmpDetailsList() != null && !empBean.getEmpDetailsList().isEmpty()) {
            addData(empBean.getEmpDetailsList());
            erBean.setResponseMessage("Successfully added " + empBean.getEmpDetailsList().size() + " employee data records");
            erBean.setResponseStatusCode(HttpStatus.CREATED);
        } else {
            erBean.setResponseMessage("Given list was empty thus there are no additions to the database");
            erBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(erBean);
    }

    // method to search for an employee details based on employee id
    @GetMapping(value = "/searchEmployeeData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseBean> searchEmployeeDetailData(@RequestBody EmployeeDetailBean empBean) {
        EmployeeResponseBean erBean = new EmployeeResponseBean();
        Optional<EmployeeDetailData> index = searchData(empBean.getEmp().getEmpId());
        if (index.isPresent()) {
            erBean.setEmp(index.get());
            erBean.setResponseMessage("Successfully found Employee Id " + empBean.getEmp().getEmpId() + " data records");
            erBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            erBean.setResponseMessage("Employee Id " + empBean.getEmp().getEmpId() + " was not found.");
            erBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(erBean);
    }

    // method to update the employee details based on employee id
    @PutMapping(value = "/updateEmployeeData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseBean> updateEmployeeDetailData(@RequestBody EmployeeDetailBean empBean) {
        EmployeeResponseBean erBean = new EmployeeResponseBean();
        if ((searchData(empBean.getEmpDetailsList().get(0).getEmpId())).isPresent()) {
            addData(empBean.getEmpDetailsList());
            erBean.setResponseMessage("Successfully updated Employee Id " + empBean.getEmpDetailsList().size() + " data records");
            erBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            erBean.setResponseMessage("Employee Id " + empBean.getEmpDetailsList().get(0).getEmpId() + " was not found.");
            erBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(erBean);
    }

    // method to remove the employee details from the database or local class variable
    @DeleteMapping(value = "/deleteEmployeeData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EmployeeResponseBean> deleteEmployeeDetailData(@RequestBody EmployeeDetailBean empBean) {
        EmployeeResponseBean erBean = new EmployeeResponseBean();
        if ((searchData(empBean.getEmp().getEmpId()).isPresent())) {
            employeeRepository.deleteById(empBean.getEmp().getEmpId());
            erBean.setResponseMessage("Successfully deleted Employee Id " + empBean.getEmp().getEmpId() + " data records");
            erBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            erBean.setResponseMessage("Employee Id " + empBean.getEmp().getEmpId() + " was not found.");
            erBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(erBean);
    }
}
