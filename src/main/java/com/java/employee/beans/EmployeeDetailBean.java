package com.java.employee.beans;

import java.util.ArrayList;

import com.java.employee.repository.EmployeeDetailData;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class EmployeeDetailBean {
    @JsonProperty("emp")
    EmployeeDetailData emp;

    @JsonProperty("empDetailsList")
    ArrayList<EmployeeDetailData> empDetailsList;

    public EmployeeDetailBean() {
    }

    public EmployeeDetailBean(EmployeeDetailData emp, ArrayList<EmployeeDetailData> empDetailsList) {
        this.emp = emp;
        this.empDetailsList = empDetailsList;
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
}
