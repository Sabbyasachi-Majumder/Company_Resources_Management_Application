package com.company.project.service;

import com.company.project.dto.ApiResponseDTO;
import com.company.project.dto.ProjectDTO;
import com.company.project.dto.ProjectResponseDTO;
import com.company.project.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface ProjectService {

    ApiResponseDTO<String> testDatabaseConnection();

    ProjectDTO toDTO(ProjectEntity entity);
    ProjectEntity toEntity(ProjectDTO dto);

    ApiResponseDTO<List<ProjectDTO>> fetchPagedDataList(int page,int size);
    Page<ProjectDTO> fetchPageData(Pageable pageable);

    ApiResponseDTO<ProjectResponseDTO> addDataToDataBase(ArrayList<ProjectDTO> empList);
    void addData(ProjectEntity employee);

    ApiResponseDTO<ProjectResponseDTO> searchDataBase(int employeeId);
    ProjectEntity searchData(int employeeId);

    ApiResponseDTO<ProjectResponseDTO> updateDataToDataBase(ArrayList<ProjectDTO> empList);

    ApiResponseDTO<ProjectResponseDTO> deleteDataFromDataBase(ArrayList<ProjectDTO> empList);
}
