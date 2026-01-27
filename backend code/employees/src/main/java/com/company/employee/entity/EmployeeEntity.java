package com.company.employee.entity;

//entity class to correspond with employees table to manipulate data

import jakarta.persistence.*;
//For input validation checks
import jakarta.validation.constraints.*;
//For lombok annotations
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

//Lombok Annotations
@Data
@Entity
@Table(name = "employees")
public class EmployeeEntity {
    @Id
    @Positive(message = "EmployeeId must be Positive.")
// @GeneratedValue(strategy = GenerationType.IDENTITY) Commenting out now to manually create employeeId . will later use this to generate employeeId automatically .
    @Column(name = "EmployeeID", nullable = false)
    private Long employeeId;

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

    @Column(name = "ManagerEmployeeID")
    @PositiveOrZero(message = "Manager Employee ID must be positive or zero(zero for employees with not manager for now")
    private Long managerEmployeeId;
}
