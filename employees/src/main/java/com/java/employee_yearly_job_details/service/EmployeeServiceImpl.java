package com.java.employee.service;

import com.java.employee.repository.Employees;
import com.java.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    public EmployeeRepository employeeRepository;

    //Adds Employee details to the employee table
    public void addData(ArrayList<Employees> empList) {
        for (Employees e : empList) {
            employeeRepository.save(e);
        }
    }

    //Searching for employee details using empId
    public Optional<Employees> searchData(int empId) {
        return employeeRepository.findById(empId);
    }
}
