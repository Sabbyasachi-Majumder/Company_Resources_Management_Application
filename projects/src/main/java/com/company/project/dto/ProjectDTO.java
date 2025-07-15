package com.company.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    @Id
    @Column(name = "ProjectId", nullable = false, length = 50)
    private int projectId;

    @NotBlank(message = "Project name cannot be empty")
    @Size(max = 50, message = "Project name must be at most 50 characters")
    @Column(name = "ProjectName", nullable = false, length = 50)
    private String projectName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Start Date cannot be null")
    @Past(message = "Start Date must be in the past")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Start Date cannot be null")
    private Date endDate;

    @Positive(message = "Manager Employee ID must be positive")
    private int departmentId;
}
