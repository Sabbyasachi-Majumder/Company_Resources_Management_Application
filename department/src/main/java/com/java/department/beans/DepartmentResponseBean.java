package com.java.department.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.department.respository.Department;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentResponseBean {

    @JsonProperty("responseMessage")
    public String responseMessage;

    @JsonProperty("responseStatusCode")
    public HttpStatus responseStatusCode;

    @JsonProperty("dept")
    public Department dept;

    @JsonProperty("deptList")
    ArrayList<Department> deptList;

    public DepartmentResponseBean() {
    }

    public DepartmentResponseBean(String responseMessage, HttpStatus responseStatusCode, Department dept, ArrayList<Department> deptList) {
        this.responseMessage = responseMessage;
        this.responseStatusCode = responseStatusCode;
        this.dept = dept;
        this.deptList = deptList;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public HttpStatus getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(HttpStatus responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public Department getDept() {
        return dept;
    }

    public void setDept(Department dept) {
        this.dept = dept;
    }

    public ArrayList<Department> getDeptList() {
        return deptList;
    }

    public void setDeptList(ArrayList<Department> deptList) {
        this.deptList = deptList;
    }
}
