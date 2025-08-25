package com.company.project.controller.testUtils;

import com.company.project.dto.ProjectDTO;
import com.company.project.dto.ProjectRequestDTO;
import com.company.project.entity.ProjectEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

//Keeping any utility functions for test classes
public class TestUtils {

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);

    //for making various project records depending on the test case
    public ProjectDTO getSampleProjectDTO(int projectId, String projectName, Date startDate, Date endDate , int departmentId) {
        ProjectDTO sampleProjectDTO = new ProjectDTO();
        sampleProjectDTO.setProjectId(projectId);
        sampleProjectDTO.setProjectName(projectName);
        sampleProjectDTO.setStartDate(startDate);
        sampleProjectDTO.setEndDate(endDate);
        sampleProjectDTO.setDepartmentId(departmentId);
        return sampleProjectDTO;
    }

    //for making various project records depending on the test case
    public ProjectRequestDTO getSampleProjectRequest(int projectId, String projectName, Date startDate, Date endDate, int departmentId) {
        ProjectRequestDTO sampleProjectRequest = new ProjectRequestDTO();
        sampleProjectRequest.setPrjDetailsList(new ArrayList<>(List.of(getSampleProjectDTO(projectId, projectName, startDate, endDate, departmentId))));
        return sampleProjectRequest;
    }

    //for making various project records depending on the test case
    public ProjectEntity getSampleProjectEntity(int projectId, String projectName, Date startDate, Date endDate, int departmentId) {
        ProjectEntity sampleProjectEntity = new ProjectEntity();
        sampleProjectEntity.setProjectId(projectId);
        sampleProjectEntity.setProjectName(projectName);
        sampleProjectEntity.setStartDate(startDate);
        sampleProjectEntity.setEndDate(endDate);
        sampleProjectEntity.setDepartmentId(departmentId);
        return sampleProjectEntity;
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
}
