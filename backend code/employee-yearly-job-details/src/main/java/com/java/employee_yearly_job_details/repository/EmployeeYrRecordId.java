package com.java.employee_yearly_job_details.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.time.Year;

/*IDClass should have only those variables which are primary keys and nothing else otherwise throws errors*/

public class EmployeeYrRecordId implements Serializable {
    @Id
    @Column(name = "EmployeeID",nullable = false, length = 50)
    private int employeeID;

    @Id
    @Column(name = "Year",nullable = false, length = 50)
    private Year year;

    public EmployeeYrRecordId() {
    }

    public EmployeeYrRecordId(int employeeID, Year year) {
        this.employeeID = employeeID;
        this.year = year;
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

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
