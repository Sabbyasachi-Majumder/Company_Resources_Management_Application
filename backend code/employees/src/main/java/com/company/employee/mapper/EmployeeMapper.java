package com.company.employee.mapper;

import com.company.employee.dto.BulkUpdateRequest;
import com.company.employee.entity.EmployeeEntity;
import com.company.employee.dto.EmployeeDTO;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",  // Spring bean injection
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,  // Skip nulls on updates
        unmappedTargetPolicy = ReportingPolicy.IGNORE  // Ignore unmapped fields (no errors)
)
public interface EmployeeMapper {
    //Fetch or Create DTO -> Employee Entity
    EmployeeEntity toEmployeeEntity(EmployeeDTO dto);

    //Employee Entity -> Fetch or Create DTO
    EmployeeDTO toFetchORCreateDto(EmployeeEntity entity);

    // Bulk update DTO -> Employee Entity
    @Mapping(target = "employeeId", ignore = true)
    void fromUpdateDtoToEntity(BulkUpdateRequest dto, @MappingTarget EmployeeEntity e);
}
