package com.company.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @Column(name = "ProjectId", nullable = false, length = 50)
    private int projectId;

    @NotBlank(message = "Project name cannot be empty")
    @Size(max = 50, message = "Project name must be at most 50 characters")
    @Column(name = "ProjectName", nullable = false, length = 50)
    private String projectName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "StartDate", nullable = false)
    @NotNull(message = "Start Date cannot be null")
    @Past(message = "Start Date must be in the past")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "EndDate", nullable = false)
    @NotNull(message = "Start Date cannot be null")
    private Date endDate;

    @Column(name = "DepartmentId", nullable = false)
    @Positive(message = "Manager Employee ID must be positive")
    private int departmentId;
}
