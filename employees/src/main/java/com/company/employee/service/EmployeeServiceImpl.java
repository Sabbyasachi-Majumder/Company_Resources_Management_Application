package com.company.employee.service;

import com.company.employee.dto.EmployeeDTO;
import com.company.employee.entity.EmployeeEntity;
import com.company.employee.repository.EmployeeRepository;
import jakarta.persistence.EntityExistsException;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;

@NoArgsConstructor(force = true)
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

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

    // Fetches all data without pagination
    @Override
    public Page<EmployeeDTO> fetchPageData(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(this::toDTO);
    }

    //Adds Employee details to the employee table
    @Override
    public void addData(EmployeeEntity entity) {
        logger.debug("Attempting to add employeeId {}", entity.getEmployeeId());
        if (employeeRepository.existsById(entity.getEmployeeId())) {
            throw new EntityExistsException("Employee ID " + entity.getEmployeeId() + " already exists");
        }
        employeeRepository.save(entity);
        logger.debug("Added employeeId {} successfully", entity.getEmployeeId());
    }

    //Update the employee record data
    @Override
    public void updateData(EmployeeEntity entity) {
        logger.debug("Attempting to update employeeId {}", entity.getEmployeeId());
        if (!employeeRepository.existsById(entity.getEmployeeId())) {
            throw new NoSuchElementException("employeeId " + entity.getEmployeeId() + " not found");
        }
        employeeRepository.save(entity);
        logger.debug("Updated employeeId {} successfully", entity.getEmployeeId());
    }

    @Override
    public EmployeeEntity searchData(int employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("employeeId " + employeeId + " not found"));
    }

    // Deletes Employee data using their employeeIds
    @Transactional
    @Override
    public void deleteAll(ArrayList<EmployeeDTO> empList) {
        for (EmployeeDTO e : empList) {
            if (employeeRepository.existsById(e.getEmployeeId()))
                employeeRepository.deleteById(e.getEmployeeId());
            else
                throw new NoSuchElementException("employeeId " + e.getEmployeeId() + " not found");
        }
    }

}
