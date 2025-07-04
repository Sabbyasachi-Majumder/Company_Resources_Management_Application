package com.company.department.entity;

//entity class to correspond with departments table to manipulate data

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

//Lombok Annotations
@Data
@Document(collection = "departments")
public class DepartmentEntity {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY) Commenting out now to manually create departmentId . will later use this to generate departmentId automatically .
    private int departmentId;

    @NotBlank(message = "Department Name cannot be empty")
    @Size(max = 50, message = "Department name must be at most 50 characters")
    private String departmentName;

    @Size(max = 50, message = "At least Location should be there")
    private List<String> locations;

    @Positive(message = "Department Head Id must be positive")
    private int departmentHeadId;

    @NotNull(message = "There should be at least one employee in each department")
    private List<Integer> departmentEmployeeIds;
}
