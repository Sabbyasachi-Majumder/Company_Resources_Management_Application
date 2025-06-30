package com.company.employee.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Schema(description = "DTO representing employee details")
public class EmployeeDTO {

    @PositiveOrZero(message = "Employee ID must be positive or zero for new employees")
    @Schema(description = "Unique identifier of the employee", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int employeeId;

    @NotBlank(message = "First name cannot be empty")
    @Size(max = 50, message = "First name must be at most 50 characters")
    @Schema(description = "First name of the employee", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Schema(description = "Last name of the employee", example = "Doe", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String lastName;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Date of birth of the employee (yyyy-MM-dd)", example = "1990-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date dateOfBirth;

    @NotBlank(message = "Gender cannot be empty")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    @Schema(description = "Gender of the employee", example = "Male", requiredMode = Schema.RequiredMode.REQUIRED, allowableValues = {"Male", "Female", "Other"})
    private String gender;

    @Positive(message = "Salary must be positive")
    @Schema(description = "Salary of the employee", example = "50000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private double salary;

    @NotNull(message = "Hire date cannot be null")
    @PastOrPresent(message = "Hire date must be in the past or present")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Hire date of the employee (yyyy-MM-dd)", example = "2023-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date hireDate;

    @NotBlank(message = "Job stage cannot be empty")
    @Size(max = 4, message = "Job stage must be at most 4 characters")
    @Schema(description = "Job stage of the employee", example = "L1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String jobStage;

    @NotBlank(message = "Designation cannot be empty")
    @Size(max = 100, message = "Designation must be at most 100 characters")
    @Schema(description = "Designation of the employee", example = "Software Engineer", requiredMode = Schema.RequiredMode.REQUIRED)
    private String designation;

    @Positive(message = "Manager Employee ID must be positive")
    @Schema(description = "Employee ID of the manager", example = "2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)  //temporarily kept not required . will be required before prod
    private int managerEmployeeId;
}
