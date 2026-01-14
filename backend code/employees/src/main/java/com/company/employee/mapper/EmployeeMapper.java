package com.company.employee.mapper;

import com.company.employee.entity.EmployeeEntity;
import com.company.employee.dto.EmployeeFetchOrCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",  // Spring bean injection
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,  // Skip nulls on updates
        unmappedTargetPolicy = ReportingPolicy.IGNORE  // Ignore unmapped fields (no errors)
)
public interface EmployeeMapper {
    //Fetch or Create DTO -> Employee Entity
    EmployeeEntity toEmployeeEntity(EmployeeFetchOrCreateRequest dto);

    //Employee Entity -> Fetch or Create DTO
    EmployeeFetchOrCreateRequest toFetchORCreateDto(EmployeeEntity entity);
}
