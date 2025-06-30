package com.company.employee.service;

import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.EmployeeResponseDTO;
import com.company.employee.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface EmployeeService {

    ApiResponseDTO<String> testDatabaseConnection();

    EmployeeDTO toDTO(EmployeeEntity entity);
    EmployeeEntity toEntity(EmployeeDTO dto);

    ApiResponseDTO<List<EmployeeDTO>> fetchPagedDataList(Pageable pageable);
    Page<EmployeeDTO> fetchPageData(Pageable pageable);

    ApiResponseDTO<EmployeeResponseDTO> addDataToDataBase(ArrayList<EmployeeDTO> empList);
    void addData(EmployeeEntity employee);

    ApiResponseDTO<EmployeeResponseDTO> searchDataBase(int employeeId);
    EmployeeEntity searchData(int employeeId);

    ApiResponseDTO<EmployeeResponseDTO> updateDataToDataBase(ArrayList<EmployeeDTO> empList);

    ApiResponseDTO<EmployeeResponseDTO> deleteDataFromDataBase(ArrayList<EmployeeDTO> empList);
}
