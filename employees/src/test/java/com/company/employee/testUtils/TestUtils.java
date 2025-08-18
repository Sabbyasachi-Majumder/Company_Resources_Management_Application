package com.company.employee.testUtils;

import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.EmployeeRequestDTO;
import com.company.employee.entity.EmployeeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//Keeping any utility functions for test classes
public class TestUtils {

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(TestUtils.class);

    //for making various employee records depending on the test case
    public EmployeeDTO getSampleEmployeeDTO(int employeeId, String firstName, String lastName, Date dateOfBirth, String gender, double salary, Date hireDate, String jobStage, String designation, int managerEmployeeId) {
        EmployeeDTO sampleEmployeeDTO = new EmployeeDTO();
        sampleEmployeeDTO.setEmployeeId(employeeId);
        sampleEmployeeDTO.setFirstName(firstName);
        sampleEmployeeDTO.setLastName(lastName);
        sampleEmployeeDTO.setDateOfBirth(dateOfBirth);
        sampleEmployeeDTO.setGender(gender);
        sampleEmployeeDTO.setSalary(salary);
        sampleEmployeeDTO.setHireDate(hireDate);
        sampleEmployeeDTO.setJobStage(jobStage);
        sampleEmployeeDTO.setDesignation(designation);
        sampleEmployeeDTO.setManagerEmployeeId(managerEmployeeId);
        return sampleEmployeeDTO;
    }

    //for making various employee records depending on the test case
    public EmployeeRequestDTO getSampleEmployeeRequest(int employeeId, String firstName, String lastName, Date dateOfBirth, String gender, double salary, Date hireDate, String jobStage, String designation, int managerEmployeeId) {
        EmployeeRequestDTO sampleEmployeeRequest = new EmployeeRequestDTO();
        sampleEmployeeRequest.setEmpDetailsList(new ArrayList<>(List.of(getSampleEmployeeDTO(employeeId, firstName, lastName, dateOfBirth, gender, salary, hireDate, jobStage, designation, managerEmployeeId))));
        return sampleEmployeeRequest;
    }

    //for making various employee records depending on the test case
    public EmployeeEntity getSampleEmployeeEntity(int employeeId, String firstName, String lastName, Date dateOfBirth, String gender, double salary, Date hireDate, String jobStage, String designation, int managerEmployeeId) {
        EmployeeEntity sampleEmployeeEntity = new EmployeeEntity();
        sampleEmployeeEntity.setEmployeeId(employeeId);
        sampleEmployeeEntity.setFirstName(firstName);
        sampleEmployeeEntity.setLastName(lastName);
        sampleEmployeeEntity.setDateOfBirth(dateOfBirth);
        sampleEmployeeEntity.setGender(gender);
        sampleEmployeeEntity.setSalary(salary);
        sampleEmployeeEntity.setHireDate(hireDate);
        sampleEmployeeEntity.setJobStage(jobStage);
        sampleEmployeeEntity.setDesignation(designation);
        sampleEmployeeEntity.setManagerEmployeeId(managerEmployeeId);
        return sampleEmployeeEntity;
    }

    //Adds Employee details to the employee table
    public EmployeeDTO toDTO(EmployeeEntity entity) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setSalary(entity.getSalary());
        dto.setHireDate(entity.getHireDate());
        dto.setJobStage(entity.getJobStage());
        dto.setDesignation(entity.getDesignation());
        dto.setManagerEmployeeId(entity.getManagerEmployeeId());
        logger.debug("Mapped entity to DTO");
        return dto;
    }

    //Adds Employee table details to the employee details
    public EmployeeEntity toEntity(EmployeeDTO dto) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setGender(dto.getGender());
        entity.setSalary(dto.getSalary());
        entity.setHireDate(dto.getHireDate());
        entity.setJobStage(dto.getJobStage());
        entity.setDesignation(dto.getDesignation());
        entity.setManagerEmployeeId(dto.getManagerEmployeeId());
        logger.debug("Mapped DTO to entity");
        return entity;
    }
}
