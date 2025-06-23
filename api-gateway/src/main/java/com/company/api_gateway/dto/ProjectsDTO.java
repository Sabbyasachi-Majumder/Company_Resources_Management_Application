package com.company.api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.sql.Date;


@Component
public class ProjectsDTO {

    @JsonProperty("ProjectID")
    private int projectID;

    @JsonProperty("ProjectName")
    private String projectName;

    @JsonProperty("StartDate")
    private Date startDate;

    @JsonProperty("EndDate")
    private Date endDate;

    @JsonProperty("DepartmentID")
    private int departmentID;

    private ProjectsDTO() {
    }

    public ProjectsDTO(int projectID, String projectName, Date startDate, int departmentID, Date endDate) {
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
