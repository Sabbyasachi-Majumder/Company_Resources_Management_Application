package com.company.employee.service;

import com.company.employee.dto.ColumnMetadata;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.mapper.EmployeeMapper;
import com.company.employee.repository.EmployeeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class EmployeeUserServiceImpl implements EmployeeUserService {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeUserServiceImpl(ObjectMapper objectMapper, ResourceLoader resourceLoader, EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
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
    public Page<EmployeeDTO> fetchPagedDataList(int page, int size, String sortByColumnName, String sortOrder) {
        Pageable pageable = PageRequest.of(page - 1, size, sortOrder.equalsIgnoreCase("ASC") ? Sort.by(sortByColumnName).ascending() : Sort.by(sortByColumnName).descending().and(Sort.by("employeeId").ascending()));  //internally the page index starts from 0 instead of 1
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


    @Override
    public List<ColumnMetadata> getEmployeeColumnMetadata() {
        Resource resource = resourceLoader.getResource("classpath:metadata.json");

        if (!resource.exists() || !resource.isReadable()) {
            throw new IllegalStateException("Metadata file not found or not readable: metadata.json");
        }

        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(
                    inputStream,
                    new TypeReference<>() {
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load employee columns metadata from JSON", e);
        }
    }
}
