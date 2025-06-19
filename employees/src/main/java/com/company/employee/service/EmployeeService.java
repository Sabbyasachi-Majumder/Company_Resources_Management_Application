package com.company.employee.service;

import com.company.employee.dto.EmployeeDTO;
import com.company.employee.entity.EmployeeEntity;

import java.util.ArrayList;
import java.util.List;

public interface EmployeeService {

    EmployeeDTO toDTO(EmployeeEntity entity);

    EmployeeEntity toEntity(EmployeeDTO dto);

    ArrayList<EmployeeDTO> fetchData();

    void addData(EmployeeEntity employee);

    void updateData(EmployeeEntity entity);

    void deleteAll(ArrayList<EmployeeDTO> empList);

    void deleteWebAll(List<Integer> empList);

    EmployeeEntity searchData(int employeeId);
}
