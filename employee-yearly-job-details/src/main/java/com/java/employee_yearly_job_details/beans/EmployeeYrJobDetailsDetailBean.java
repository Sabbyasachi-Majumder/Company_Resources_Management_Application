package com.java.employee_yearly_job_details.beans;

import java.util.ArrayList;

import com.java.employee_yearly_job_details.repository.EmployeesYrJobDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class EmployeeYrJobDetailsDetailBean {

    @JsonProperty("eyj")
    EmployeesYrJobDetails eyj;

    @JsonProperty("eyjDetailsList")
    ArrayList<EmployeesYrJobDetails> eyjDetailsList;

    @JsonProperty("searchFor")
    String searchFor;

    public EmployeeYrJobDetailsDetailBean() {
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

    public String getSearchFor() {
        return searchFor;
    }

    public void setSearchFor(String searchFor) {
        this.searchFor = searchFor;
    }

    public EmployeeYrJobDetailsDetailBean(EmployeesYrJobDetails eyj, ArrayList<EmployeesYrJobDetails> eyjDetailsList,String searchFor) {
        this.eyj = eyj;
        this.eyjDetailsList = eyjDetailsList;
        this.searchFor=searchFor;
    }
}
