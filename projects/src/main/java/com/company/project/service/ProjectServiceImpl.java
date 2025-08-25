package com.company.project.service;

import com.company.project.dto.ApiResponseDTO;
import com.company.project.dto.ProjectDTO;
import com.company.project.dto.ProjectResponseDTO;
import com.company.project.entity.ProjectEntity;
import com.company.project.repository.ProjectRepository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@AllArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final DataSource dataSource;

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    //Test Database Connection business logic
    public ApiResponseDTO<String> testDatabaseConnection() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            if (connection.isValid(1)) {
                logger.debug("Testing successful . Database connection is present.");
                return new ApiResponseDTO<>("success", "Connection from Project Application to Project Database successfully established.", null);
            } else {
                logger.error("Testing failed . Database connection is not present.");
                return new ApiResponseDTO<>("error", "Connection to Project Database failed to be established.", null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Adds Project details to the project table
    public ProjectDTO toDTO(ProjectEntity entity) {
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(entity.getProjectId());
        dto.setProjectName(entity.getProjectName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setDepartmentId(entity.getDepartmentId());
        logger.debug("Mapped entity to DTO");
        return dto;
    }

    //Adds Project table details to the project details
    public ProjectEntity toEntity(ProjectDTO dto) {
        ProjectEntity entity = new ProjectEntity();
        entity.setProjectId(dto.getProjectId());
        entity.setProjectName(dto.getProjectName());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setDepartmentId(dto.getDepartmentId());
        logger.debug("Mapped DTO to entity");
        return entity;
    }

    public ApiResponseDTO<List<ProjectDTO>> fetchPagedDataList(int page,int size) {
        Pageable pageable = PageRequest.of(page - 1, size);  //internally the page index starts from 0 instead of 1
        Page<ProjectDTO> pagedData = fetchPageData(pageable);
        List<ProjectDTO> currentData = pagedData.getContent();
        if (pageable.getPageNumber() < 0 || pageable.getPageNumber() > Math.ceil((float) pagedData.getTotalElements() / pageable.getPageSize()))
            throw new IllegalArgumentException();
        return new ApiResponseDTO<>("success", "Fetching page " + (pageable.getPageNumber() + 1) + " with " + currentData.size() + " Project data records", currentData);
    }

    // Fetches all data with pagination
    @Override
    public Page<ProjectDTO> fetchPageData(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(this::toDTO);
    }

    // Business logic to add project data records one by one .
    public ApiResponseDTO<ProjectResponseDTO> addDataToDataBase(ArrayList<ProjectDTO> prjList) {
        ArrayList<ApiResponseDTO<ProjectResponseDTO>> responses = new ArrayList<>();
        int addCounter = 0;
        for (ProjectDTO e : prjList) {
            if (!projectRepository.existsById(e.getProjectId())) {
                logger.debug("Adding projectId {} ", e.getProjectId());
                addData(toEntity(e));
                addCounter++;
                responses.add(new ApiResponseDTO<>("success", "Successfully added Project Id " + e.getProjectId() + " data records", null));
            } else {
                logger.error("projectId {} is already present thus not added again.", e.getProjectId());
                responses.add(new ApiResponseDTO<>("error", "Project Id " + e.getProjectId() + " already exists ", null));
            }
        }
        return new ApiResponseDTO<>("success", "Successfully added " + addCounter + " . Add failed : " + (prjList.size() - addCounter), new ProjectResponseDTO(null, responses));
    }

    //Adds Project details to the project table
    @Override
    public void addData(ProjectEntity entity) {
        logger.debug("Attempting to add projectId {}", entity.getProjectId());
        assert projectRepository != null;
        projectRepository.save(entity);
        logger.debug("Added projectId {} successfully", entity.getProjectId());
    }

    // Business logic to search database for a project based on its projectId
    public ApiResponseDTO<ProjectResponseDTO> searchDataBase(int projectId) {
        ArrayList<ProjectDTO> entityArrayList = new ArrayList<>();
        entityArrayList.add(toDTO(searchData(projectId)));
        return new ApiResponseDTO<>("success", "Successfully found Project Id " + projectId + " data records", new ProjectResponseDTO(entityArrayList, null));
    }

    // Calling findById to search the table for a project based on projectId
    @Override
    public ProjectEntity searchData(int projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("projectId " + projectId + " not found"));
    }

    public ApiResponseDTO<ProjectResponseDTO> updateDataToDataBase(ArrayList<ProjectDTO> prjList) {
        ArrayList<ApiResponseDTO<ProjectResponseDTO>> responses = new ArrayList<>();
        int updateCounter = 0;
        for (ProjectDTO e : prjList) {
            if (projectRepository.existsById(e.getProjectId())) {
                logger.debug("Updated projectId {} successfully", e.getProjectId());
                projectRepository.save(toEntity(e));
                updateCounter++;
                responses.add(new ApiResponseDTO<>("success", "Successfully updated Project Id " + e.getProjectId() + " data records", null));
            } else {
                logger.error("Updating projectId {} failed since projectId doesn't exist", e.getProjectId());
                responses.add(new ApiResponseDTO<>("error", "Project Id " + e.getProjectId() + " doesn't exist", null));
            }
        }
        return new ApiResponseDTO<>("success", "Update Success : " + updateCounter + " . Update Failed : " + (prjList.size() - updateCounter), new ProjectResponseDTO(null, responses));
    }

    public ApiResponseDTO<ProjectResponseDTO> deleteDataFromDataBase(ArrayList<ProjectDTO> prjList) {
        ArrayList<ApiResponseDTO<ProjectResponseDTO>> responses = new ArrayList<>();
        int deleteCounter = 0;
        for (ProjectDTO e : prjList) {
            ApiResponseDTO<ProjectResponseDTO> apiResponse;
            if (projectRepository.existsById(e.getProjectId())) {
                logger.debug("Deleted projectId {} successfully", e.getProjectId());
                projectRepository.deleteById(e.getProjectId());
                deleteCounter++;
                apiResponse = new ApiResponseDTO<>("success", "Successfully deleted Project Id " + e.getProjectId() + " data records", null);
            } else {
                logger.error("Deleting projectId {} failed since projectId doesn't exist", e.getProjectId());
                apiResponse = new ApiResponseDTO<>("error", "Project Id " + e.getProjectId() + " doesn't exist", null);
            }
            responses.add(apiResponse);
        }
        return new ApiResponseDTO<>("success", "Delete Success : " + deleteCounter + ". Delete Failed : " + (prjList.size() - deleteCounter), new ProjectResponseDTO(null, responses));
    }
}
