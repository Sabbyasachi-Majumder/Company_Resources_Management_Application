package com.company.employee.DTOs;

import java.util.ArrayList;

import com.company.employee.Entity.EmployeeEntity;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class EmployeeRequestDTO {
    @Valid
    @JsonProperty("emp")
    EmployeeEntity emp;

    @Valid
    @JsonProperty("empDetailsList")
    ArrayList<EmployeeEntity> empDetailsList;
}
