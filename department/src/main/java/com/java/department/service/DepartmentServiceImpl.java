package com.java.department.service;

import com.java.department.respository.Department;
import com.java.department.respository.DepartmentRespository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;

public class DepartmentServiceImpl implements DepartmentService{
    @Autowired
    public DepartmentRespository departmentRespository;

    //Adds Employee details to the employee table
    public void addData(ArrayList<Department> empList) {
        for (Department e : empList) {
            departmentRespository.save(e);
        }
    }

    //Searching for employee details using empId
    public Optional<Department> searchData(int empId) {
        return departmentRespository.findById(empId);
    }
}
