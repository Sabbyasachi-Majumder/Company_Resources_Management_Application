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
@Schema(description = "Generic API response DTO containing status, message, and data")
public class ApiResponseDTO<T> {

    @Schema(description = "Status of the response", example = "success", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"success", "error"})
    private String status; // "success" or "error"

    @Schema(description = "Message describing the result", example = "Operation successful", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "Data payload of the response", example = "null", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private T data;
}
