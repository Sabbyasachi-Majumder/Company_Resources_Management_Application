package com.java.employee_yearly_job_details.beans;

import java.util.ArrayList;

import com.java.employee_yearly_job_details.repository.EmployeesYrJobDetails;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class EmployeeYrJobDetailsResponseBean {

    @JsonProperty("responseMessage")
    public String responseMessage;

    @JsonProperty("responseStatusCode")
    public HttpStatus responseStatusCode;

    @JsonProperty("eyj")
    EmployeesYrJobDetails eyj;

    @JsonProperty("eyjDetailsList")
    ArrayList<EmployeesYrJobDetails> eyjDetailsList;

    public EmployeeYrJobDetailsResponseBean() {
    }

    public EmployeeYrJobDetailsResponseBean(String responseMessage, HttpStatus responseStatusCode, EmployeesYrJobDetails eyj, ArrayList<EmployeesYrJobDetails> eyjDetailsList) {
        this.responseMessage = responseMessage;
        this.responseStatusCode = responseStatusCode;
        this.eyj = eyj;
        this.eyjDetailsList = eyjDetailsList;
    }

    public HttpStatus getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(HttpStatus responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }

    public EmployeesYrJobDetails getEyj() {
        return eyj;
    }

    public void setEyj(EmployeesYrJobDetails eyj) {
        this.eyj = eyj;
    }

    public ArrayList<EmployeesYrJobDetails> getEyjDetailsList() {
        return eyjDetailsList;
    }

    public void setEyjDetailsList(ArrayList<EmployeesYrJobDetails> eyjDetailsList) {
        this.eyjDetailsList = eyjDetailsList;
    }



    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
