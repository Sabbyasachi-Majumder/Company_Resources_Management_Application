package com.company.project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Schema(description = "Request DTO for employee operations, containing a list of project details")
public class ProjectRequestDTO {

    @Valid
    @NotEmpty(message = "Project details list cannot be empty")
    @JsonProperty("prjDetailsList")
    @Schema(description = "List of project details", requiredMode = Schema.RequiredMode.REQUIRED)
    ArrayList<ProjectDTO> prjDetailsList;
}
