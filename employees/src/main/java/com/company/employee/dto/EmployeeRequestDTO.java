package com.company.employee.dto;

import java.util.ArrayList;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Schema(description = "Request DTO for employee operations, containing a list of employee details")
public class EmployeeRequestDTO {

    @Valid
    @NotEmpty(message = "Employee details list cannot be empty")
    @JsonProperty("empDetailsList")
    @Schema(description = "List of employee details", requiredMode = Schema.RequiredMode.REQUIRED)
    ArrayList<EmployeeDTO> empDetailsList;
}
