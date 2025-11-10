package com.company.department.testUtils;

import com.company.department.dto.DepartmentDTO;
import com.company.department.dto.DepartmentRequestDTO;
import com.company.department.entity.DepartmentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//Keeping any utility functions for test classes
public class TestUtils {

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);

    //for making various department records depending on the test case
    public DepartmentDTO getSampleDepartmentDTO(int departmentId, String departmentName, List<String> locations, int departmentHeadId , List<Integer> departmentEmployeeIds) {
        DepartmentDTO sampleDepartmentDTO = new DepartmentDTO();
        sampleDepartmentDTO.setDepartmentId(departmentId);
        sampleDepartmentDTO.setDepartmentName(departmentName);
        sampleDepartmentDTO.setLocations(locations);
        sampleDepartmentDTO.setDepartmentHeadId(departmentHeadId);
        sampleDepartmentDTO.setDepartmentEmployeeIds(departmentEmployeeIds);
        return sampleDepartmentDTO;
    }

    //for making various department records depending on the test case
    public DepartmentRequestDTO getSampleDepartmentRequest(int departmentId, String departmentName, List<String> locations, int departmentHeadId , List<Integer> departmentEmployeeIds) {
        DepartmentRequestDTO sampleDepartmentRequest = new DepartmentRequestDTO();
        sampleDepartmentRequest.setDepartmentDetailList(new ArrayList<>(List.of(getSampleDepartmentDTO(departmentId, departmentName, locations, departmentHeadId ,  departmentEmployeeIds))));
        return sampleDepartmentRequest;
    }

    //for making various department records depending on the test case
    public DepartmentEntity getSampleDepartmentEntity(int departmentId, String departmentName, List<String> locations, int departmentHeadId , List<Integer> departmentEmployeeIds) {
        DepartmentEntity sampleDepartmentEntity = new DepartmentEntity();
        sampleDepartmentEntity.setDepartmentId(departmentId);
        sampleDepartmentEntity.setDepartmentName(departmentName);
        sampleDepartmentEntity.setLocations(locations);
        sampleDepartmentEntity.setDepartmentHeadId(departmentHeadId);
        sampleDepartmentEntity.setDepartmentEmployeeIds(departmentEmployeeIds);
        return sampleDepartmentEntity;
    }

    //Adds Department details to the department table
    public DepartmentDTO toDTO(DepartmentEntity entity) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setDepartmentId(entity.getDepartmentId());
        dto.setDepartmentName(entity.getDepartmentName());
        dto.setLocations(entity.getLocations());
        dto.setDepartmentHeadId(entity.getDepartmentHeadId());
        dto.setDepartmentEmployeeIds(entity.getDepartmentEmployeeIds());
        logger.debug("Mapped entity to DTO");
        return dto;
    }

    //Adds Department table details to the department details
    public DepartmentEntity toEntity(DepartmentDTO dto) {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setDepartmentId(dto.getDepartmentId());
        entity.setDepartmentName(dto.getDepartmentName());
        entity.setLocations(dto.getLocations());
        entity.setDepartmentHeadId(dto.getDepartmentHeadId());
        entity.setDepartmentId(dto.getDepartmentId());
        logger.debug("Mapped DTO to entity");
        return entity;
    }
}
