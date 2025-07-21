package com.company.department.service;

import com.company.department.dto.DepartmentDTO;
import com.company.department.dto.ApiResponseDTO;
import com.company.department.dto.DepartmentResponseDTO;
import com.company.department.entity.DepartmentEntity;
import com.company.department.repository.DepartmentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@NoArgsConstructor(force = true)
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private final DepartmentRepository DepartmentRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    //Test Database Connection business logic
    public ApiResponseDTO<String> testDatabaseConnection() {
        Document result = mongoTemplate.getDb().runCommand(new Document("ping", 1));
        if (result != null && result.containsKey("ok") && result.getDouble("ok") == 1.0) {
            logger.info("Testing successful . Database connection is present.");
            return new ApiResponseDTO<>("success", "Connection from department Application to department Database successfully established.", null);
        } else {
            logger.info("Testing successful . Database connection is present.");
            return new ApiResponseDTO<>("success", "Connection from department Application to department Database successfully established.", null);
        }
    }

    //Adds department details to the department table
    public DepartmentDTO toDTO(DepartmentEntity entity) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setDepartmentId(entity.getDepartmentId());
        dto.setDepartmentName(entity.getDepartmentName());
        dto.setLocations(entity.getLocations());
        dto.setDepartmentHeadId(entity.getDepartmentHeadId());
        dto.setDepartmentEmployeeIds(entity.getDepartmentEmployeeIds());
        logger.info("Mapped entity to DTO");
        return dto;
    }

    //Adds department table details to the department details
    public DepartmentEntity toEntity(DepartmentDTO dto) {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setDepartmentId(dto.getDepartmentId());
        entity.setDepartmentName(dto.getDepartmentName());
        entity.setLocations(dto.getLocations());
        entity.setDepartmentHeadId(dto.getDepartmentHeadId());
        entity.setDepartmentEmployeeIds(dto.getDepartmentEmployeeIds());
        logger.info("Mapped DTO to entity");
        return entity;
    }

    public ApiResponseDTO<List<DepartmentDTO>> fetchPagedDataList(Pageable pageable) {
        Page<DepartmentDTO> pagedData = fetchPageData(pageable);
        if (pageable.getPageNumber()< Math.ceil((float) pagedData.getTotalElements() / pageable.getPageSize())) {
            List<DepartmentDTO> currentData = pagedData.getContent();
            return new ApiResponseDTO<>("success", "Fetching page " + (pageable.getPageNumber()+1) + " with " + currentData.size() + " department data records", currentData);
        } else
            return new ApiResponseDTO<>("success", "Total number of records is lower than the current page number " + (pageable.getPageNumber()+1) + " containing " + pageable.getPageSize() + " department data records each page.", null);

    }

    // Fetches all data with pagination
    @Override
    public Page<DepartmentDTO> fetchPageData(Pageable pageable) {
        return DepartmentRepository.findAll(pageable)
                .map(this::toDTO);
    }

    // Business logic to add department data records one by one .
    public ApiResponseDTO<DepartmentResponseDTO> addDataToDataBase(@Valid @NotEmpty ArrayList<DepartmentDTO> empList) {
        ArrayList<ApiResponseDTO<DepartmentResponseDTO>> responses = new ArrayList<>();
        int addCounter = 0;
        for (DepartmentDTO e : empList) {
            if (!DepartmentRepository.existsById(e.getDepartmentId())) {
                logger.info("Adding departmentId {} ", e.getDepartmentId());
                addData(toEntity(e));
                addCounter++;
                responses.add(new ApiResponseDTO<>("success", "Successfully added department Id " + e.getDepartmentId() + " data records", null));
            } else {
                logger.error("departmentId {} is already present thus not added again.", e.getDepartmentId());
                responses.add(new ApiResponseDTO<>("error", "department Id " + e.getDepartmentId() + " already exists ", null));
            }
        }
        return new ApiResponseDTO<>("success", "Successfully added " + addCounter + " . Add failed : " + (empList.size() - addCounter), new DepartmentResponseDTO(null, responses));
    }

    //Adds department details to the department table
    @Override
    public void addData(DepartmentEntity entity) {
        logger.info("Attempting to add departmentId {}", entity.getDepartmentId());
        assert DepartmentRepository != null;
        DepartmentRepository.save(entity);
        logger.info("Added departmentId {} successfully", entity.getDepartmentId());
    }

    // Business logic to search database for a department based on its departmentId
    public ApiResponseDTO<DepartmentResponseDTO> searchDataBase(int departmentId) {
        ArrayList<DepartmentDTO> entityArrayList = new ArrayList<>();
        entityArrayList.add(toDTO(searchData(departmentId)));
        return new ApiResponseDTO<>("success", "Successfully found department Id " + departmentId + " data records", new DepartmentResponseDTO(entityArrayList, null));
    }

    // Calling findById to search the table for a department based on departmentId
    @Override
    public DepartmentEntity searchData(int departmentId) {
        return DepartmentRepository.findById(departmentId)
                .orElseThrow(() -> new NoSuchElementException("departmentId " + departmentId + " not found"));
    }

    public ApiResponseDTO<DepartmentResponseDTO> updateDataToDataBase(@Valid @NotEmpty ArrayList<DepartmentDTO> empList) {
        ArrayList<ApiResponseDTO<DepartmentResponseDTO>> responses = new ArrayList<>();
        int updateCounter = 0;
        for (DepartmentDTO e : empList) {
            if (DepartmentRepository.existsById(e.getDepartmentId())) {
                logger.info("Updated departmentId {} successfully", e.getDepartmentId());
                DepartmentRepository.save(toEntity(e));
                updateCounter++;
                responses.add(new ApiResponseDTO<>("success", "Successfully updated department Id " + e.getDepartmentId() + " data records", null));
            } else {
                logger.error("Updating departmentId {} failed since departmentId doesn't exist", e.getDepartmentId());
                responses.add(new ApiResponseDTO<>("error", "department Id " + e.getDepartmentId() + " doesn't exist", null));
            }
        }
        return new ApiResponseDTO<>("success", "Update Success : " + updateCounter + " . Update Failed : " + (empList.size() - updateCounter), new DepartmentResponseDTO(null, responses));
    }

    public ApiResponseDTO<DepartmentResponseDTO> deleteDataFromDataBase(@Valid @NotEmpty ArrayList<DepartmentDTO> empList) {
        ArrayList<ApiResponseDTO<DepartmentResponseDTO>> responses = new ArrayList<>();
        int deleteCounter = 0;
        for (DepartmentDTO e : empList) {
            ApiResponseDTO<DepartmentResponseDTO> apiResponse;
            if (DepartmentRepository.existsById(e.getDepartmentId())) {
                logger.info("Deleted departmentId {} successfully", e.getDepartmentId());
                DepartmentRepository.deleteById(e.getDepartmentId());
                deleteCounter++;
                apiResponse = new ApiResponseDTO<>("success", "Successfully deleted department Id " + e.getDepartmentId() + " data records", null);
            } else {
                logger.error("Deleting departmentId {} failed since departmentId doesn't exist", e.getDepartmentId());
                apiResponse = new ApiResponseDTO<>("error", "department Id " + e.getDepartmentId() + " doesn't exist", null);
            }
            responses.add(apiResponse);
        }
        return new ApiResponseDTO<>("success", "Delete Success : " + deleteCounter + ". Delete Failed : " + (empList.size() - deleteCounter), new DepartmentResponseDTO(null, responses));
    }
}
