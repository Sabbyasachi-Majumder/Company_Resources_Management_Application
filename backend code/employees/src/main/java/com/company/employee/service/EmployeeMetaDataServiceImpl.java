package com.company.employee.service;

import com.company.employee.dto.ColumnMetadata;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class EmployeeMetaDataServiceImpl implements EmployeeMetaDataService {

    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    public EmployeeMetaDataServiceImpl(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
        this.objectMapper = objectMapper;
        this.resourceLoader = resourceLoader;
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
