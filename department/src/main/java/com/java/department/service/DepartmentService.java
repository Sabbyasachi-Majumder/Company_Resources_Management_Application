package com.java.department.service;

import com.java.department.respository.Department;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public interface DepartmentService {
    public void addData(ArrayList<Department> empList);

    public Optional<Department> searchData(int empId);
}
