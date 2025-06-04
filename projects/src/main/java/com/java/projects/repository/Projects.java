package com.java.projects.repository;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Entity
@Table(name = "projects")
public class Projects {

    @Id
    @Column(name = "ProjectID", nullable = false, length = 50)
    private int projectID;

    @Column(name = "ProjectName", nullable = false, length = 50)
    private String projectName;

    @Column(name = "StartDate", length = 50)
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "EndDate", nullable = true, precision = 10)
    private Date endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DepartmentID", nullable = false)
    private int departmentID;

    private Projects() {
    }

    public Projects(int projectID, String projectName, Date startDate, int departmentID, Date endDate) {
        this.projectID = projectID;
        this.projectName = projectName;
        this.startDate = startDate;
        this.departmentID = departmentID;
        this.endDate = endDate;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }
}
