package com.company.employee.Services;

import com.company.employee.Entity.EmployeeEntity;

import java.util.ArrayList;
import java.util.Optional;

public interface EmployeeService {

    void addData(EmployeeEntity employeeEntity);
    void deleteAll(ArrayList<EmployeeEntity> empList);
    EmployeeEntity searchData(int employeeId);
}
