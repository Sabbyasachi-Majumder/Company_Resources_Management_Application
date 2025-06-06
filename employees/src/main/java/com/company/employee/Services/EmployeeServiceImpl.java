package com.company.employee.Services;

import com.company.employee.Entity.EmployeeEntity;
import com.company.employee.Repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    public EmployeeRepository employeeRepository;

    //Adds Employee details to the employee table
    @Transactional
    @Override
    public void addData(ArrayList<EmployeeEntity> empList) {
        employeeRepository.saveAll(empList);
    }

    @Override
    public Optional<EmployeeEntity> searchData(int employeeId) {
        return Optional.ofNullable(employeeRepository.findById(employeeId).orElseThrow(() -> new NoSuchElementException("Employee with ID " + employeeId + " not found")));
    }
}
