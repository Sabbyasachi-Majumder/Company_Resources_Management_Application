package com.company.employee.dto;

import java.util.ArrayList;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Schema(description = "Response DTO for employee operations, containing a list of employees or operation responses")
public class EmployeeResponseDTO {

    @JsonProperty("empDetailsList")
    @Schema(description = "List of employee details returned in  the response", example = "null", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    ArrayList<EmployeeDTO> empDetailsList;

    @JsonProperty("apiResponse")
    @Schema(description = "List of API responses for individual operations (e.g., add, update, delete)", example = "null", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    ArrayList<ApiResponseDTO<EmployeeResponseDTO>> apiResponse;
}
