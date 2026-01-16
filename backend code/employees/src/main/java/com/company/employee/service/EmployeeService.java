package com.company.employee.service;

import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.dto.EmployeeFetchOrCreateDTO;
import com.company.employee.dto.EmployeeResponseDTO;
import com.company.employee.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    String testDatabaseConnection();

    Page<EmployeeFetchOrCreateDTO> fetchPagedDataList(int page, int size);

    Page<EmployeeFetchOrCreateDTO> fetchPageData(Pageable pageable);

    EmployeeFetchOrCreateDTO searchDataBase(Long employeeId);

    EmployeeEntity searchData(Long employeeId);

//    EmployeeFetchOrCreateDTO addDataToDataBase(List<EmployeeFetchOrCreateDTO> employeeFetchOrCreateRequests);
//
//    void addData(EmployeeEntity employee);
//
//    ApiResponseDTO<EmployeeResponseDTO> updateDataToDataBase(List<BulkUpdateRequest> bulkUpdateRequestList);
//
//    ApiResponseDTO<EmployeeResponseDTO> deleteDataFromDataBase(List<Long> deleteEmployeesList);
}
