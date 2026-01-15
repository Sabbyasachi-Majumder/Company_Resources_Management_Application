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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Used for noting down the Error Details for System failure")
public class ErrorDetailsDto {

    @Schema(description = "Error Code for the System Crash")
    private String errorCode;

    @Schema(description = "Error Message for the System Crash")
    private String errorMessage;
}
