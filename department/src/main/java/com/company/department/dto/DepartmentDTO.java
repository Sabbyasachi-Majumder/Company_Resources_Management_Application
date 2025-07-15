package com.company.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@Schema(description = "DTO representing Department details")
public class DepartmentDTO {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY) Commenting out now to manually create departmentId . will later use this to generate departmentId automatically .
    private int departmentId;

    //    @NotBlank(message = "Department Name cannot be empty")
//    @Size(max = 50, message = "Department name must be at most 50 characters")
    private String departmentName;

    //    @Size(max = 50, message = "At least one Location should be there")
    private List<String> locations;

    //    @Positive(message = "Department Head Id must be positive")
    private int departmentHeadId;

    //    @NotNull(message = "There should be at least one employee in each department")
    private List<Integer> departmentEmployeeIds;
}
