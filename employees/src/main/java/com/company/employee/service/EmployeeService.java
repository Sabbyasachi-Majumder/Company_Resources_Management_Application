package com.company.employee.service;

import com.company.employee.dto.EmployeeDTO;
import com.company.employee.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface EmployeeService {

    EmployeeDTO toDTO(EmployeeEntity entity);

    EmployeeEntity toEntity(EmployeeDTO dto);

    Page<EmployeeDTO> fetchPageData(Pageable pageable);

    void addData(EmployeeEntity employee);

    void updateData(EmployeeEntity entity);

    void deleteAll(ArrayList<EmployeeDTO> empList);

    void deleteWebAll(List<Integer> empList);

    EmployeeEntity searchData(int employeeId);
}
