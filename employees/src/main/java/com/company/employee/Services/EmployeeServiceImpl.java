package com.company.employee.Services;

import com.company.employee.Entity.EmployeeEntity;
import com.company.employee.Repositories.EmployeeRepository;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor(force = true)
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    //Adds Employee details to the employee table
    @Transactional
    @Override
    public void addData(EmployeeEntity employeeEntity) {
        logger.debug("Attempting to add employeeId {} ", employeeEntity.getEmployeeId());
        employeeRepository.save(employeeEntity);
        logger.debug("Added employeeId {} successfully", employeeEntity.getEmployeeId());
    }

    @Override
    public EmployeeEntity searchData(int employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NullPointerException("employeeId " + employeeId + " not found"));
    }

    // Deletes Employee data using their employeeIds
    @Transactional
    @Override
    public void deleteAll(ArrayList<EmployeeEntity> empList) {
        for (EmployeeEntity e : empList) {
            if (employeeRepository.existsById(e.getEmployeeId()))
                employeeRepository.deleteById(e.getEmployeeId());
            else
                throw new NoSuchElementException("employeeId " + e.getEmployeeId() + " not found");
        }
    }
}
