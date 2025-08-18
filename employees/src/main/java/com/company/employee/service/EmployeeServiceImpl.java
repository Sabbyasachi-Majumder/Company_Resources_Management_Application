package com.company.employee.service;

import com.company.employee.dto.ApiResponseDTO;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.EmployeeResponseDTO;
import com.company.employee.entity.EmployeeEntity;
import com.company.employee.repository.EmployeeRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.sql.DataSource;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DataSource dataSource;

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DataSource dataSource) {
        this.employeeRepository = employeeRepository;
        this.dataSource = dataSource;
    }

    //Test Database Connection business logic
    public ApiResponseDTO<String> testDatabaseConnection() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            if (connection.isValid(1)) {
                logger.debug("Testing successful . Database connection is present.");
                return new ApiResponseDTO<>("success", "Connection from Employee Application to Employee Database successfully established.", null);
            } else {
                logger.error("Testing failed . Database connection is not present.");
                return new ApiResponseDTO<>("error", "Connection to Employee Database failed to be established.", null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public ApiResponseDTO<List<EmployeeDTO>> fetchPagedDataList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);  //internally the page index starts from 0 instead of 1
        Page<EmployeeDTO> pagedData = fetchPageData(pageable);
        List<EmployeeDTO> currentData = pagedData.getContent();
        if (pageable.getPageNumber() < 0 || pageable.getPageNumber() > Math.ceil((float) pagedData.getTotalElements() / pageable.getPageSize()))
            throw new IllegalArgumentException();
        return new ApiResponseDTO<>("success", "Fetching page " + (pageable.getPageNumber() + 1) + " with " + currentData.size() + " Employee data records", currentData);
    }

    // Fetches all data with pagination
    @Override
    public Page<EmployeeDTO> fetchPageData(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(this::toDTO);
    }

    // Business logic to add employee data records one by one .
    public ApiResponseDTO<EmployeeResponseDTO> addDataToDataBase(ArrayList<EmployeeDTO> empList) {
        ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
        int addCounter = 0;
        for (EmployeeDTO e : empList) {
            if (!employeeRepository.existsById(e.getEmployeeId())) {
                logger.debug("Adding employeeId {} ", e.getEmployeeId());
                addData(toEntity(e));
                addCounter++;
                responses.add(new ApiResponseDTO<>("success", "Successfully added Employee Id " + e.getEmployeeId() + " data records", null));
            } else {
                logger.error("employeeId {} is already present thus not added again.", e.getEmployeeId());
                responses.add(new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " already exists ", null));
            }
        }
        return new ApiResponseDTO<>("success", "Successfully added " + addCounter + " . Add failed : " + (empList.size() - addCounter), new EmployeeResponseDTO(null, responses));
    }

    //Adds Employee details to the employee table
    @Override
    public void addData(EmployeeEntity entity) {
        logger.debug("Attempting to add employeeId {}", entity.getEmployeeId());
        assert employeeRepository != null;
        employeeRepository.save(entity);
        logger.debug("Added employeeId {} successfully", entity.getEmployeeId());
    }

    // Business logic to search database for an employee based on its employeeId
    public ApiResponseDTO<EmployeeResponseDTO> searchDataBase(int employeeId) {
        ArrayList<EmployeeDTO> entityArrayList = new ArrayList<>();
        entityArrayList.add(toDTO(searchData(employeeId)));
        return new ApiResponseDTO<>("success", "Successfully found Employee Id " + employeeId + " data records", new EmployeeResponseDTO(entityArrayList, null));
    }

    // Calling findById to search the table for a employee based on employeeId
    @Override
    public EmployeeEntity searchData(int employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("employeeId " + employeeId + " not found"));
    }

    public ApiResponseDTO<EmployeeResponseDTO> updateDataToDataBase(ArrayList<EmployeeDTO> empList) {
        ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
        int updateCounter = 0;
        for (EmployeeDTO e : empList) {
            if (employeeRepository.existsById(e.getEmployeeId())) {
                logger.debug("Updated employeeId {} successfully", e.getEmployeeId());
                employeeRepository.save(toEntity(e));
                updateCounter++;
                responses.add(new ApiResponseDTO<>("success", "Successfully updated Employee Id " + e.getEmployeeId() + " data records", null));
            } else {
                logger.error("Updating employeeId {} failed since employeeId doesn't exist", e.getEmployeeId());
                responses.add(new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " doesn't exist", null));
            }
        }
        return new ApiResponseDTO<>("success", "Update Success : " + updateCounter + " . Update Failed : " + (empList.size() - updateCounter), new EmployeeResponseDTO(null, responses));
    }

    public ApiResponseDTO<EmployeeResponseDTO> deleteDataFromDataBase(ArrayList<EmployeeDTO> empList) {
        ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
        int deleteCounter = 0;
        for (EmployeeDTO e : empList) {
            ApiResponseDTO<EmployeeResponseDTO> apiResponse;
            if (employeeRepository.existsById(e.getEmployeeId())) {
                logger.debug("Deleted employeeId {} successfully", e.getEmployeeId());
                employeeRepository.deleteById(e.getEmployeeId());
                deleteCounter++;
                apiResponse = new ApiResponseDTO<>("success", "Successfully deleted Employee Id " + e.getEmployeeId() + " data records", null);
            } else {
                logger.error("Deleting employeeId {} failed since employeeId doesn't exist", e.getEmployeeId());
                apiResponse = new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " doesn't exist", null);
            }
            responses.add(apiResponse);
        }
        return new ApiResponseDTO<>("success", "Delete Success : " + deleteCounter + ". Delete Failed : " + (empList.size() - deleteCounter), new EmployeeResponseDTO(null, responses));
    }
}
