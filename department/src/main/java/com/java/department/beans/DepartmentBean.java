package com.java.department.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.department.respository.Department;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DepartmentBean {

    @JsonProperty(namespace ="department")
    public Department department;
    @JsonProperty(namespace ="deptList")
    public ArrayList<Department> deptList;

    public DepartmentBean(){}

    public DepartmentBean(Department department, ArrayList<Department> deptList) {
        this.department = department;
        this.deptList = deptList;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public ArrayList<Department> getDeptList() {
        return deptList;
    }

    public void setDeptList(ArrayList<Department> deptList) {
        this.deptList = deptList;
    }

}
