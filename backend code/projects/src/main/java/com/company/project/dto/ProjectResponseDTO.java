package com.company.project.dto;

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
@Schema(description = "Response DTO for project operations, containing a list of project or operation responses")
public class ProjectResponseDTO {

    @JsonProperty("prjDetailsList")
    @Schema(description = "List of project details returned in the response", example = "null", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    ArrayList<ProjectDTO> prjDetailsList;

    @JsonProperty("apiResponse")
    @Schema(description = "List of API responses for individual operations (e.g., add, update, delete)", example = "null", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    ArrayList<ApiResponseDTO<ProjectResponseDTO>> apiResponse;
}
