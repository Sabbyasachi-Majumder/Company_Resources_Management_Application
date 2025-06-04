package com.java.department.controller;

import com.java.department.beans.DepartmentBean;
import com.java.department.beans.DepartmentResponseBean;
import com.java.department.respository.Department;
import com.java.department.respository.DepartmentRespository;
import com.java.department.service.DepartmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

@RestController
public class DepartmentController {
    @Autowired
    public DepartmentRespository departmentRespository;

    @Autowired
    public DepartmentServiceImpl departmentService;

    @Autowired
    private DataSource dataSource;

    // testing connection
    @GetMapping(value = "/testDepartmentConnection")
    public ResponseEntity<DepartmentResponseBean> testPostmanToDepartmentApplicationConnection() {
        DepartmentResponseBean dmBean = new DepartmentResponseBean();
        dmBean.setResponseMessage("Connection successfully established.");
        dmBean.setResponseStatusCode(HttpStatus.OK);
        return returnResponse(dmBean);
    }

    // testing Database connection
    @GetMapping(value = "/testDepartmentDataBaseConnection")
    public ResponseEntity<DepartmentResponseBean> testDepartmentDataBaseConnection() throws SQLException {
        DepartmentResponseBean dmBean = new DepartmentResponseBean();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        if (connection.isValid(1)) {
            dmBean.setResponseMessage("Database Connection successfully established.");
            dmBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            dmBean.setResponseMessage("Database Connection failed.");
            dmBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(dmBean);
    }

    // returning the response bean with adequate status code , message , custom header and response body information
    public ResponseEntity<DepartmentResponseBean> returnResponse(DepartmentResponseBean dmBean) {
        return ResponseEntity
                .status(dmBean.getResponseStatusCode()) // Set status code
                .header("URL", "/printDepartmentListJson")
                .body(dmBean);  // Set response body
    }

    //    Fetching the database table data on request
    @GetMapping(value = "/fetchDepartmentDetailData", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DepartmentResponseBean> fetchDepartment() {
        ArrayList<Department> deptList = (ArrayList<Department>) departmentRespository.findAll();
        DepartmentResponseBean dmBean = new DepartmentResponseBean();
        if (!deptList.isEmpty()) {
            dmBean.setDeptList(deptList);
            dmBean.setResponseMessage("Fetching " + deptList.size() + " Department data records");
            dmBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            dmBean.setResponseMessage("Given list was empty thus no data can be fetched");
            dmBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(dmBean);
    }

    //method to add the Department details to the database
    @PostMapping(value = "/addDepartmentDetailData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DepartmentResponseBean> addDepartment(@RequestBody DepartmentBean deptBean) {
        DepartmentResponseBean dmBean = new DepartmentResponseBean();
        if (deptBean.getDeptList() != null && !deptBean.getDeptList().isEmpty()) {
            departmentService.addData(deptBean.getDeptList());
            dmBean.setResponseMessage("Successfully added " + deptBean.getDeptList().size() + " Department data records");
            dmBean.setResponseStatusCode(HttpStatus.CREATED);
        } else {
            dmBean.setResponseMessage("Given list was empty thus there are no additions to the database");
            dmBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(dmBean);
    }

    // method to search for Department details based on Department id
    @GetMapping(value = "/searchDepartmentData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DepartmentResponseBean> searchDepartment(@RequestBody DepartmentBean deptBean) {
        DepartmentResponseBean dmBean = new DepartmentResponseBean();
        Optional<Department> index = departmentService.searchData(deptBean.getDepartment().getDepartmentID());
        if (index.isPresent()) {
            dmBean.setDept(index.get());
            dmBean.setResponseMessage("Successfully found Department Id " + deptBean.getDepartment().getDepartmentID() + " data records");
            dmBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            dmBean.setResponseMessage("Department Id " + deptBean.getDepartment().getDepartmentID() + " was not found.");
            dmBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(dmBean);
    }

    // method to update the Department details based on Department id
    @PutMapping(value = "/updateDepartmentData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DepartmentResponseBean> updateDepartment(@RequestBody DepartmentBean deptBean) {
        DepartmentResponseBean dmBean = new DepartmentResponseBean();
        if ((departmentService.searchData(deptBean.getDeptList().get(0).getDepartmentID())).isPresent()) {
            departmentService.addData(deptBean.getDeptList());
            dmBean.setResponseMessage("Successfully updated Department Id " + deptBean.getDeptList().get(0).getDepartmentID() + " data records");
            dmBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            dmBean.setResponseMessage("Department Id " + deptBean.getDeptList().get(0).getDepartmentID() + " was not found.");
            dmBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(dmBean);
    }

    // method to remove the Department details from the database or local class variable
    @DeleteMapping(value = "/deleteDepartmentData", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DepartmentResponseBean> deleteDepartment(@RequestBody DepartmentBean deptBean) {
        DepartmentResponseBean dmBean = new DepartmentResponseBean();
        if ((departmentService.searchData(deptBean.getDepartment().getDepartmentID()).isPresent())) {
            departmentRespository.deleteById(deptBean.getDepartment().getDepartmentID());
            dmBean.setResponseMessage("Successfully deleted Department Id " + deptBean.getDepartment().getDepartmentID() + " data records");
            dmBean.setResponseStatusCode(HttpStatus.OK);
        } else {
            dmBean.setResponseMessage("Department Id " + deptBean.getDepartment().getDepartmentID() + " was not found.");
            dmBean.setResponseStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return returnResponse(dmBean);
    }
}
