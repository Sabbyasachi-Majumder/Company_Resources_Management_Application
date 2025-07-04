package com.company.department.entity;

//entity class to correspond with departments table to manipulate data

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

//Lombok Annotations
@Data
@Entity
@Table(name = "departments")
public class DepartmentEntity {
    @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY) Commenting out now to manually create departmentId . will later use this to generate departmentId automatically .
    @Column(name = "departmentID", nullable = false)
    private int departmentId;

    @Column(name = "FirstName", nullable = false, length = 50)
    @NotBlank(message = "First name cannot be empty")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @Column(name = "LastName", length = 50)
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "DateOfBirth", nullable = false)
    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;

    @Column(name = "Gender", nullable = false, length = 6)
    @NotBlank(message = "Gender cannot be empty")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @Column(name = "Salary", nullable = false)
    @Positive(message = "Salary must be positive")
    private double salary;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @Column(name = "HireDate", nullable = false)
    @NotNull(message = "Hire date cannot be null")
    @PastOrPresent(message = "Hire date must be in the past or present")
    private Date hireDate;

    @Column(name = "JobStage", nullable = false, length = 4)
    @NotBlank(message = "Job stage cannot be empty")
    @Size(max = 4, message = "Job stage must be at most 4 characters")
    private String jobStage;

    @Column(name = "Designation", nullable = false)
    @NotBlank(message = "Designation cannot be empty")
    @Size(max = 100, message = "Designation must be at most 100 characters")
    private String designation;

    @Column(name = "ManagerDepartmentID")
    @Positive(message = "Manager department ID must be positive")
    private int managerDepartmentId;
}
