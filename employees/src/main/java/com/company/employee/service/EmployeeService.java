package com.company.employee.service;

import com.company.employee.dto.EmployeeDTO;
import com.company.employee.entity.EmployeeEntity;

import java.util.ArrayList;

public interface EmployeeService {

    EmployeeDTO toDTO(EmployeeEntity entity);

    EmployeeEntity toEntity(EmployeeDTO dto);

    public ArrayList<EmployeeDTO> fetchData();

    void addData(EmployeeEntity employee);

    void deleteAll(ArrayList<EmployeeDTO> empList);

    EmployeeEntity searchData(int employeeId);
}
