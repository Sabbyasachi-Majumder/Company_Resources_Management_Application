package com.java.employee.beans;

import java.util.ArrayList;

import com.java.employee.repository.Employees;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class EmployeeDetailBean {
    @JsonProperty("emp")
    Employees emp;

    @JsonProperty("empDetailsList")
    ArrayList<Employees> empDetailsList;

    public EmployeeDetailBean() {
    }

    public EmployeeDetailBean(Employees emp, ArrayList<Employees> empDetailsList) {
        this.emp = emp;
        this.empDetailsList = empDetailsList;
    }

    public Employees getEmp() {
        return emp;
    }

    public void setEmp(Employees emp) {
        this.emp = emp;
    }

    public ArrayList<Employees> getEmpDetailsList() {
        return empDetailsList;
    }

    public void setEmpDetailsList(ArrayList<Employees> empDetailsList) {
        this.empDetailsList = empDetailsList;
    }
}
