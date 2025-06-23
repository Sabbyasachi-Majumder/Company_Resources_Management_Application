package com.company.api_gateway.beans;

import com.company.api_gateway.dto.EmployeeDTO;
import com.company.api_gateway.dto.ProjectsDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JoinedQueryResponseBean {

    @JsonProperty("responseMessage")
    public String responseMessage;

    @JsonProperty("responseStatusCode")
    public HttpStatus responseStatusCode;

    @JsonProperty("emp")
    EmployeeDTO emp;

    @JsonProperty("empDetailsList")
    ArrayList<EmployeeDTO> empDetailsList;

    @JsonProperty("prj")
    ProjectsDTO prj;

    @JsonProperty("prj")
    ArrayList<ProjectsDTO> prjDetailsList;

    public JoinedQueryResponseBean() {
    }

    public JoinedQueryResponseBean(String responseMessage, HttpStatus responseStatusCode, EmployeeDTO emp, ArrayList<EmployeeDTO> empDetailsList) {
        this.responseMessage = responseMessage;
        this.responseStatusCode = responseStatusCode;
        this.emp = emp;
        this.empDetailsList = empDetailsList;
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

    public ArrayList<EmployeeDTO> getEmpDetailsList() {
        return empDetailsList;
    }

    public void setEmpDetailsList(ArrayList<EmployeeDTO> empDetailsList) {
        this.empDetailsList = empDetailsList;
    }

    public EmployeeDTO getEmp() {
        return emp;
    }

    public void setEmp(EmployeeDTO emp) {
        this.emp = emp;
    }

    public ProjectsDTO getPrj() {
        return prj;
    }

    public void setPrj(ProjectsDTO prj) {
        this.prj = prj;
    }

    public ArrayList<ProjectsDTO> getPrjDetailsList() {
        return prjDetailsList;
    }

    public void setPrjDetailsList(ArrayList<ProjectsDTO> prjDetailsList) {
        this.prjDetailsList = prjDetailsList;
    }
}
