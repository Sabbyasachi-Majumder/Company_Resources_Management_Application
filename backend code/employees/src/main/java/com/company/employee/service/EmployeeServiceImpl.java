package com.company.employee.service;

import com.company.employee.dto.EmployeeFetchOrCreateDTO;
import com.company.employee.entity.EmployeeEntity;
import com.company.employee.mapper.EmployeeMapper;
import com.company.employee.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.sql.DataSource;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DataSource dataSource;
    private final EmployeeMapper employeeMapper;

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DataSource dataSource, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.dataSource = dataSource;
        this.employeeMapper = employeeMapper;
    }

    //Test Database Connection business logic
    public String testDatabaseConnection() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            if (connection.isValid(1)) {
                logger.debug("Testing successful . Database connection is present.");
                return "Connection from Employee Application to Employee Database successfully established.";
            } else {
                logger.error("Testing failed . Database connection is not present.");
                return "Connection to Employee Database failed to be established.";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Get Employee Data Table with Pageable specifications
    public Page<EmployeeFetchOrCreateDTO> fetchPagedDataList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);  //internally the page index starts from 0 instead of 1
        Page<EmployeeFetchOrCreateDTO> pagedData = fetchPageData(pageable);
        if (pageable.getPageNumber() < 0 || pageable.getPageNumber() > Math.ceil((float) pagedData.getTotalElements() / pageable.getPageSize()))
            throw new IllegalArgumentException();
        return pagedData;
    }

    // Fetches all data with pagination
    public Page<EmployeeFetchOrCreateDTO> fetchPageData(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(employeeMapper::toFetchORCreateDto);
    }

    // Business logic to search database for an employee based on its employeeId
    public EmployeeFetchOrCreateDTO searchDataBase(Long employeeId) {
        return employeeMapper.toFetchORCreateDto(searchData(employeeId));
    }

    // Calling findById to search the table for a employee based on employeeId
    public EmployeeEntity searchData(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("employeeId " + employeeId + " not found"));
    }


    // Business logic to add employee data records one by one .
//    public ApiResponseDTO<EmployeeResponseDTO> addDataToDataBase(List<EmployeeFetchOrCreateDTO> employeeFetchOrCreateRequestList) {
//        ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
//        Long addCounter = 0;
//        for (EmployeeFetchOrCreateDTO e : employeeFetchOrCreateRequestList) {
//            if (!employeeRepository.existsById(e.getEmployeeId())) {
//                logger.debug("Adding employeeId {} ", e.getEmployeeId());
//                addData();
//                addCounter++;
//                responses.add(new ApiResponseDTO<>("success", "Successfully added Employee Id " + e.getEmployeeId() + " data records", null));
//            } else {
//                logger.error("employeeId {} is already present thus not added again.", e.getEmployeeId());
//                responses.add(new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " already exists ", null));
//            }
//        }
//        return new ApiResponseDTO<>("success", "Successfully added " + addCounter + " . Add failed : " + (employeeFetchOrCreateRequestList.size() - addCounter), new EmployeeResponseDTO(null, responses));
//    }
//
//    //Adds Employee details to the employee table
//    @Override
//    public void addData(EmployeeEntity entity) {
//        logger.debug("Attempting to add employeeId {}", entity.getEmployeeId());
//        employeeRepository.save(entity);
//        logger.debug("Added employeeId {} successfully", entity.getEmployeeId());
//    }
//
//    public ApiResponseDTO<EmployeeResponseDTO> updateDataToDataBase(ArrayList<BulkUpdateRequest> bulkUpdateRequestArrayList) {
//        ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
//        Long updateCounter = 0;
//        for (BulkUpdateRequest e : bulkUpdateRequestArrayList) {
//            if (employeeRepository.existsById(e.getEmployeeId())) {
//                logger.debug("Updated employeeId {} successfully", e.getEmployeeId());
//                employeeRepository.save(toEntity(e));
//                updateCounter++;
//                responses.add(new ApiResponseDTO<>("success", "Successfully updated Employee Id " + e.getEmployeeId() + " data records", null));
//            } else {
//                logger.error("Updating employeeId {} failed since employeeId doesn't exist", e.getEmployeeId());
//                responses.add(new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " doesn't exist", null));
//            }
//        }
//        return new ApiResponseDTO<>("success", "Update Success : " + updateCounter + " . Update Failed : " + (empList.size() - updateCounter), new EmployeeResponseDTO(null, responses));
//    }
//
//    public ApiResponseDTO<EmployeeResponseDTO> deleteDataFromDataBase(ArrayList<EmployeeFetchOrCreateDTO> empList) {
//        ArrayList<ApiResponseDTO<EmployeeResponseDTO>> responses = new ArrayList<>();
//        Long deleteCounter = 0;
//        for (EmployeeFetchOrCreateDTO e : empList) {
//            ApiResponseDTO<EmployeeResponseDTO> apiResponse;
//            if (employeeRepository.existsById(e.getEmployeeId())) {
//                logger.debug("Deleted employeeId {} successfully", e.getEmployeeId());
//                employeeRepository.deleteById(e.getEmployeeId());
//                deleteCounter++;
//                apiResponse = new ApiResponseDTO<>("success", "Successfully deleted Employee Id " + e.getEmployeeId() + " data records", null);
//            } else {
//                logger.error("Deleting employeeId {} failed since employeeId doesn't exist", e.getEmployeeId());
//                apiResponse = new ApiResponseDTO<>("error", "Employee Id " + e.getEmployeeId() + " doesn't exist", null);
//            }
//            responses.add(apiResponse);
//        }
//        return new ApiResponseDTO<>("success", "Delete Success : " + deleteCounter + ". Delete Failed : " + (empList.size() - deleteCounter), new EmployeeResponseDTO(null, responses));
//    }
}
