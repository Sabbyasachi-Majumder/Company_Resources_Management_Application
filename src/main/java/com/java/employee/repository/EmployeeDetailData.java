package com.java.employee.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "employee_details")
public class EmployeeDetailData {
    @Id
    public int empId;

    @Column(name="name")
    public String name;

    @Column(name="location")
    public String location;

    @Column(name="designation")
    public String design;

    public EmployeeDetailData() {
    }

    public EmployeeDetailData(int empId, String name, String location, String design) {
        this.empId = empId;
        this.name = name;
        this.location = location;
        this.design = design;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

}
