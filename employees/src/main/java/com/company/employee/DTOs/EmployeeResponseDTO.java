package com.company.employee.DTOs;

import java.util.ArrayList;

import com.company.employee.Entity.EmployeeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class EmployeeResponseDTO {

    @JsonProperty("responseMessage")
    public String responseMessage;

    @JsonProperty("responseStatusCode")
    public HttpStatus responseStatusCode;

    @JsonProperty("emp")
    EmployeeEntity emp;

    @JsonProperty("empDetailsList")
    ArrayList<EmployeeEntity> empDetailsList;

}
