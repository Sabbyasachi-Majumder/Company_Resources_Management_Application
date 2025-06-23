package com.company.api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class EmployeeDTO {
    @JsonProperty("employeeID")
    private int employeeID;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("dateOfBirth")
    private Date dateOfBirth;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("salary")
    private double salary;

    @JsonProperty("hireDate")
    private Date hireDate;

    @JsonProperty("jobStage")
    private String jobStage;

    @JsonProperty("designation")
    private String designation;

    @JsonProperty("managerEmployeeID")
    private int managerEmployeeID;

    private EmployeeDTO() {
    }

    public EmployeeDTO(Date hireDate, double salary, String gender, Date dateOfBirth, int employeeID, String firstName, String lastName, String jobStage, String designation,int managerEmployeeID) {
        this.hireDate = hireDate;
        this.salary = salary;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.designation = designation;
        this.jobStage = jobStage;
        this.managerEmployeeID=managerEmployeeID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getJobStage() {
        return jobStage;
    }

    public void setJobStage(String jobStage) {
        this.jobStage = jobStage;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getManagerEmployeeID() {
        return managerEmployeeID;
    }

    public void setManagerEmployeeID(int managerEmployeeID) {
        this.managerEmployeeID = managerEmployeeID;
    }

}
