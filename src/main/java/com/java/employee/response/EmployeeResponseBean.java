package com.java.employee.response;

import java.util.ArrayList;

import com.java.employee.repository.EmployeeDetailData;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class EmployeeResponseBean {

    @JsonProperty("responseMessage")
    public String responseMessage;

    @JsonProperty("responseStatusCode")
    public HttpStatus responseStatusCode;

    @JsonProperty("emp")
    EmployeeDetailData emp;

    @JsonProperty("empDetailsList")
    ArrayList<EmployeeDetailData> empDetailsList;

    public EmployeeResponseBean(String responseMessage, HttpStatus responseStatusCode, EmployeeDetailData emp, ArrayList<EmployeeDetailData> empDetailsList) {
        this.responseMessage = responseMessage;
        this.responseStatusCode = responseStatusCode;
        this.emp = emp;
        this.empDetailsList = empDetailsList;
    }

    public HttpStatus getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(HttpStatus responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public EmployeeDetailData getEmp() {
        return emp;
    }

    public void setEmp(EmployeeDetailData emp) {
        this.emp = emp;
    }

    public ArrayList<EmployeeDetailData> getEmpDetailsList() {
        return empDetailsList;
    }

    public void setEmpDetailsList(ArrayList<EmployeeDetailData> empDetailsList) {
        this.empDetailsList = empDetailsList;
    }

    public EmployeeResponseBean() {
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
