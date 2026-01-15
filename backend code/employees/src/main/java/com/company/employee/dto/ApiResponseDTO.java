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

    @Schema(description = "The actual response payload (Page of employees, single employee, result summary, etc.)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private T data;

    @Schema(description = "Present only when the operation had partial success, failures, or warnings (e.g., refresh failed). Contains per-item errors and special markers (-1 = full failure, -2 = refresh warning)",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private OperationSummaryDTO operationSummaryDTO;

    @Schema(description = "Error Details for the System Crash",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private ErrorDetailsDto errorDetails;

}