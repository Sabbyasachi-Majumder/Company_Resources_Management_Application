package com.company.department.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Schema(description = "Response DTO for department operations, containing a list of departments or operation responses")
public class DepartmentResponseDTO {

    @JsonProperty("departmentDetailsList")
    @Schema(description = "List of department details returned in the response", example = "null", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    ArrayList<DepartmentDTO> departmentDetailsList;

    @JsonProperty("apiResponse")
    @Schema(description = "List of API responses for individual operations (e.g., add, update, delete)", example = "null", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    ArrayList<ApiResponseDTO<DepartmentResponseDTO>> apiResponse;
}
