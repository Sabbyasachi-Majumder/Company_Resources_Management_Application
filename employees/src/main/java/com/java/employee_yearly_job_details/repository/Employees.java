package com.java.employee.repository;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "employees")
public class Employees {
    @Id
    @Column(name = "EmployeeID", nullable = false, length = 50)
    private int employeeID;

    @Column(name = "FirstName", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LastName", length = 50)
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DateOfBirth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "Gender", nullable = false, length = 6)
    private String gender;

    @Column(name = "Salary", nullable = false, precision = 10)
    private double salary;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "HireDate", nullable = false)
    private Date hireDate;

    @Column(name = "JobStage", nullable = false, length = 3)
    private String jobStage;

    @Column(name = "Designation", nullable = false)
    private String designation;

    @Column(name = "ManagerEmployeeID", nullable = false, length = 50)
    private int managerEmployeeID;

    private Employees() {
    }

    public Employees(Date hireDate, double salary, String gender, Date dateOfBirth, int employeeID, String firstName, String lastName, String jobStage, String designation) {
        this.hireDate = hireDate;
        this.salary = salary;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.designation = designation;
        this.jobStage = jobStage;
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

}
