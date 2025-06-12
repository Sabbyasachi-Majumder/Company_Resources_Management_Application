package com.company.employee.service;

import com.company.employee.dto.EmployeeDTO;
import com.company.employee.entity.EmployeeEntity;
import com.company.employee.repository.EmployeeRepository;
import lombok.NoArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public ArrayList<EmployeeDTO> fetchData() {
        ArrayList<EmployeeDTO> response = new ArrayList<>();
        for (EmployeeEntity e : employeeRepository.findAll())
            response.add(toDTO(e));
        return response;
    }

    //Adds Employee details to the employee table
    @Transactional
    public void addData(EmployeeEntity employee) {
        logger.debug("Attempting to add employeeId {} ", employee.getEmployeeId());
        employeeRepository.save(employee);
        logger.debug("Added employeeId {} successfully", employee.getEmployeeId());
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
