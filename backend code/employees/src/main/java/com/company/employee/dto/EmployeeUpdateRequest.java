package com.company.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to add new employee detail")
public class EmployeeUpdateRequest {

    @PositiveOrZero(message = "Employee ID must be positive or zero for new employees")
    @Schema(description = "Unique identifier of the employee", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer employeeId;

    @Size(max = 50, message = "First name must be at most 50 characters")
    @Schema(description = "First name", example = "Sam", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String firstName;

    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Schema(description = "Last name", example = "White", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String lastName;

    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date of birth (yyyy-MM-dd)", example = "1990-01-01", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date dateOfBirth;

    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    @Schema(description = "Gender", example = "Male", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"Male", "Female", "Other"})
    private String gender;

    @Positive(message = "Salary must be positive")
    @Schema(description = "Starting salary", example = "50000.00", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Double salary;  //should be in employee professional information service

    @PastOrPresent(message = "Hire date must be in the past or present")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Hire date (yyyy-MM-dd)", example = "2023-01-01", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date hireDate;

    @Size(max = 4, message = "Job stage must be at most 4 characters")
    @Schema(description = "Job stage/level code", example = "JS3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String jobStage;

    @Size(max = 100, message = "Designation must be at most 100 characters")
    @Schema(description = "Job title / designation", example = "Software Developer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String designation;

    @PositiveOrZero(message = "Manager Employee ID must be positive or zero (0 = no manager)")
    @Schema(description = "Manager's employee ID (0 = no manager)", example = "0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //temporarily kept not required . will be required before prod
    private Integer managerEmployeeId = 0;  //default value is 0
}
