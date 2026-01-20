package com.company.employee.service;

import com.company.employee.dto.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // Get Total amount of Employee Data Table entries
    public Long countEntities() {
        try {
            return employeeRepository.count();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Get Employee Data Table with Pageable specifications
    public Page<EmployeeDTO> fetchPagedDataList(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);  //internally the page index starts from 0 instead of 1
        Page<EmployeeDTO> pagedData = employeeRepository.findAll(pageable)
                .map(employeeMapper::toFetchORCreateDto);
        if (pageable.getPageNumber() < 0 || pageable.getPageNumber() > Math.ceil((float) pagedData.getTotalElements() / pageable.getPageSize()))
            throw new IllegalArgumentException();
        return pagedData;
    }

    // Business logic to search database for an employee based on its employeeId
    public EmployeeDTO searchDataBase(Long employeeId) {
        return employeeMapper.toFetchORCreateDto(employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("employeeId " + employeeId + " not found")));
    }


    // Business logic to add employee data records one by one .
    public OperationSummaryDTO addDataToDataBase(List<EmployeeDTO> employeeFetchOrCreateRequestList) {
        Map<Long, String> operationDetails = new HashMap<>();
        for (EmployeeDTO e : employeeFetchOrCreateRequestList) {
            logger.debug("Creating employee with employeeID: {}", e.getEmployeeId());
            try {
                if (e.getEmployeeId() != null && employeeRepository.existsById(e.getEmployeeId()))
                    throw new IllegalArgumentException("The employeeId is either empty or present in the database.");
                employeeRepository.save(employeeMapper.toEmployeeEntity(e));
                logger.debug("Successfully created employee with employeeId {} ", e.getEmployeeId());
            } catch (Exception ex) {
                logger.error("Error while creating EmployeeId {} -> {}", e.getEmployeeId(), ex.getMessage());
                operationDetails.put(e.getEmployeeId(), ex.getMessage());
            }
        }
        return new OperationSummaryDTO((long) employeeFetchOrCreateRequestList.size(), (long) employeeFetchOrCreateRequestList.size() - operationDetails.size(), (long) operationDetails.size(), operationDetails);
    }

    public OperationSummaryDTO bulkUpdateDataToDataBase(List<BulkUpdateRequest> bulkUpdateRequestArrayList) {
        Map<Long, String> operationDetails = new HashMap<>();
        for (BulkUpdateRequest dto : bulkUpdateRequestArrayList) {
            try {
                logger.debug("Updating employeeId {} successfully", dto.getEmployeeId());
                EmployeeEntity entity = employeeRepository.findById(dto.getEmployeeId()).orElseThrow(EntityNotFoundException::new);
                employeeMapper.fromUpdateDtoToEntity(dto, entity);
                employeeRepository.save(entity);
            } catch (Exception ex) {
                logger.error("Error while updating EmployeeId {} -> {}", dto.getEmployeeId(), ex.getLocalizedMessage());
                operationDetails.put(dto.getEmployeeId(), "doesn't exist");
            }
        }
        return new OperationSummaryDTO((long) bulkUpdateRequestArrayList.size(), (long) bulkUpdateRequestArrayList.size() - operationDetails.size(), (long) operationDetails.size(), operationDetails);
    }

    public OperationSummaryDTO bulkDeleteDataFromDataBase(List<Long> employeeIds) {
        Map<Long, String> operationDetails = new HashMap<>();
        for (Long employeeId : employeeIds) {
            try {
                logger.debug("Deleted employeeId {} successfully", employeeId);
                EmployeeEntity e = employeeRepository.findById(employeeId).orElseThrow(EntityNotFoundException::new);
                employeeRepository.delete(e);
            } catch (EntityNotFoundException ex) {
                logger.error("Deleting employeeId {} failed since employeeId doesn't exist", employeeId);
                operationDetails.put(employeeId, "doesn't exist");
            }
        }
        return new OperationSummaryDTO((long) employeeIds.size(), (long) employeeIds.size() - operationDetails.size(), (long) operationDetails.size(), operationDetails);
    }
}
