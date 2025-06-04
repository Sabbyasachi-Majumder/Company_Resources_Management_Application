package com.java.employee.service;

import com.java.employee.repository.Employees;

import java.util.ArrayList;
import java.util.Optional;

public interface EmployeeService {

    public void addData(ArrayList<Employees> empList);

    public Optional<Employees> searchData(int empId);
}
