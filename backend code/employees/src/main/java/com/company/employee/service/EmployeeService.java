package com.company.employee.service;

import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.dto.BulkUpdateRequest;
import com.company.employee.dto.EmployeeFetchOrCreateRequest;
import com.company.employee.dto.EmployeeResponseDTO;
import com.company.employee.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    String testDatabaseConnection();

    ApiResponseDTO<Page<EmployeeFetchOrCreateRequest>> fetchPagedDataList(int page, int size);

    Page<EmployeeFetchOrCreateRequest> fetchPageData(Pageable pageable);

//    ApiResponseDTO<EmployeeResponseDTO> addDataToDataBase(List<EmployeeFetchOrCreateRequest> employeeFetchOrCreateRequests);
//
//    void addData(EmployeeEntity employee);
//
//    ApiResponseDTO<EmployeeResponseDTO> searchDataBase(Integer employeeId);
//
//    EmployeeEntity searchData(Integer employeeId);
//
//    ApiResponseDTO<EmployeeResponseDTO> updateDataToDataBase(List<BulkUpdateRequest> bulkUpdateRequestList);
//
//    ApiResponseDTO<EmployeeResponseDTO> deleteDataFromDataBase(List<Integer> deleteEmployeesList);
}
