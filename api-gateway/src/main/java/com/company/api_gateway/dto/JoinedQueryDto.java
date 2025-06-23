package com.company.api_gateway.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


public class JoinedQueryDto {

    private EmployeeDTO employeeData;

    // Constructor
    public JoinedQueryDto() {
        this.employeeData = fetchUserFromApplicationA();
    }

    // Method to fetch UserDTO from Application A
    private EmployeeDTO fetchUserFromApplicationA() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://application-a/api/user";
        return restTemplate.getForObject(url, EmployeeDTO.class);
    }

    public EmployeeDTO getEmployeeData() {
        return employeeData;
    }

    public void setEmployeeData(EmployeeDTO employeeData) {
        this.employeeData = employeeData;
    }

}
