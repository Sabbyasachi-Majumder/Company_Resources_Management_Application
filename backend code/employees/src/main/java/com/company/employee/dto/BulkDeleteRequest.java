package com.company.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to bulk delete employees by their IDs")
public class BulkDeleteRequest {

    @NotEmpty(message = "List of Employee Ids cannot be empty")
    @Size(max = 500, message = "Maximum 500 IDs allowed per request to prevent overload")
    @Schema(
            description = "List of Employee Id of the employees",
            example = "[1, 2, 3, 45, 67]",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("employeeIds")
    private List<Integer> employeeIds;
}
