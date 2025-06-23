package com.company.employee.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Schema(description = "Pagination details request DTO for employee operations, specifically for fetchEmployees, containing a list of employee details")
public class PaginationRequestDTO {
    /*@Valid
    @NotEmpty(message = "Page Number Cannot be empty")*/
    @JsonProperty("page")
    @Schema(description = "Page number for pagination", requiredMode = Schema.RequiredMode.REQUIRED)
    int page;

    /*@Valid
    @NotEmpty(message = "Number of records Cannot be empty")*/
    @JsonProperty("size")
    @Schema(description = "Number of records for pagination", requiredMode = Schema.RequiredMode.REQUIRED)
    int size;
}
