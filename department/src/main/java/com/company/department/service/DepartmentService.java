package com.company.department.service;

import com.company.department.dto.DepartmentDTO;
import com.company.department.dto.ApiResponseDTO;
import com.company.department.dto.DepartmentResponseDTO;
import com.company.department.entity.DepartmentEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface DepartmentService {

    ApiResponseDTO<String> testDatabaseConnection();

    DepartmentDTO toDTO(DepartmentEntity entity);

    DepartmentEntity toEntity(DepartmentDTO dto);

    ApiResponseDTO<List<DepartmentDTO>> fetchPagedDataList(Pageable pageable);

    Page<DepartmentDTO> fetchPageData(Pageable pageable);

    ApiResponseDTO<DepartmentResponseDTO> addDataToDataBase(@Valid @NotEmpty ArrayList<DepartmentDTO> empList);

    void addData(DepartmentEntity Department);

    ApiResponseDTO<DepartmentResponseDTO> searchDataBase(int DepartmentId);

    DepartmentEntity searchData(int DepartmentId);

    ApiResponseDTO<DepartmentResponseDTO> updateDataToDataBase(@Valid @NotEmpty ArrayList<DepartmentDTO> empList);

    ApiResponseDTO<DepartmentResponseDTO> deleteDataFromDataBase(@Valid @NotEmpty ArrayList<DepartmentDTO> empList);
}
