package com.company.employee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Unified API response wrapper used across all microservices")
public class ApiResponseDTO<T> {
    @Schema(
            description = "The actual payload — type depends on endpoint: Page<EmployeeDTO> on success, OperationSummaryDTO on mutation feedback, ErrorDetailsDto on failure",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private T data;
}