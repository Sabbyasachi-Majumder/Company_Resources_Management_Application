package com.company.employee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Used for noting down the partial successes and failures of any operation")
public class OperationSummaryDTO {

    @Schema(description = "Total Number of Objects sent for the Operation")
    private Long totalRequested;

    @Schema(description = "Total Number of Objects succeeded during the Operation")
    private Long successCount;

    @Schema(description = "Total Number of Objects failed during the Operation")
    private Long errorCount;

    @NotNull
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Schema(description = """
            Details of operation results:
            - Positive keys: employeeId → failure reason (partial failures)
            - Key -1: full failure (no changes applied, reason in value)
            - Key -2: mutation succeeded but table refresh failed (warning in value)
            """)
    private Map<Long, String> operationDetails = new HashMap<>();
}
