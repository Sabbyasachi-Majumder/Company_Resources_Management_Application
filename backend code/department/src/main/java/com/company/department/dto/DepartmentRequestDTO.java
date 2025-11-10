package com.company.department.dto;

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
@Schema(description = "Request DTO for department operations, containing a list of department details")
public class DepartmentRequestDTO {

    @Valid
    @NotEmpty(message = "Department details list cannot be empty")
    @JsonProperty("departmentDetailList")
    @Schema(description = "List of department details", requiredMode = Schema.RequiredMode.REQUIRED)
    ArrayList<DepartmentDTO> departmentDetailList;
}
