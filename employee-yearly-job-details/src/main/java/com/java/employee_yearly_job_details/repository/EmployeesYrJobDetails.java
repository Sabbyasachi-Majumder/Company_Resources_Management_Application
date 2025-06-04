package com.java.employee_yearly_job_details.repository;

import jakarta.persistence.*;

import java.time.Year;

@Entity
@Table(name = "employee_yearly_job_details")
@IdClass(EmployeeYrRecordId.class)
public class EmployeesYrJobDetails{
    @Id
    @Column(name = "EmployeeID",nullable = false, length = 50)
    private int employeeID;

    @Id
    @Column(name = "Year",nullable = false, length = 50)
    private Year year;

    @Column(name = "HikePercentage", length = 50)
    private float hikePercentage;

    @Column(name = "EmployeeRemarks", nullable = true)
    private String employeeRemarks;

    @Column(name = "ManagerRemarks", nullable = true)
    private String managerRemarks;

    @Column(name = "DepartmentID", nullable = false)
    private int departmentID;

    @Column(name = "ProjectID", nullable = false)
    private int projectID;

    private EmployeesYrJobDetails() {
    }

    public EmployeesYrJobDetails(EmployeeYrRecordId employeeYrRecordId, float hikePercentage, String employeeRemarks, String managerRemarks, int projectID, int departmentID) {
        //this.employeeYrRecordId = employeeYrRecordId;
        this.hikePercentage = hikePercentage;
        this.employeeRemarks = employeeRemarks;
        this.managerRemarks = managerRemarks;
        this.projectID = projectID;
        this.departmentID = departmentID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    /*public int getEmployeeYrRecordId() {
        return employeeYrRecordId.getEmployeeID();
    }

    public void setEmployeeYrRecordId(EmployeeYrRecordId employeeYrRecordId) {
        this.employeeYrRecordId = employeeYrRecordId;
    }*/

    public String getEmployeeRemarks() {
        return employeeRemarks;
    }

    public void setEmployeeRemarks(String employeeRemarks) {
        this.employeeRemarks = employeeRemarks;
    }

    public float getHikePercentage() {
        return hikePercentage;
    }

    public void setHikePercentage(float hikePercentage) {
        this.hikePercentage = hikePercentage;
    }

    public String getManagerRemarks() {
        return managerRemarks;
    }

    public void setManagerRemarks(String managerRemarks) {
        this.managerRemarks = managerRemarks;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
}
