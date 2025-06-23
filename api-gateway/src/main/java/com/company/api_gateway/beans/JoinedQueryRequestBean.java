package com.company.api_gateway.beans;

import com.company.api_gateway.dto.EmployeeDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

@Component
public class JoinedQueryRequestBean {

    @JsonProperty("emp")
    public EmployeeDTO emp;

    public JoinedQueryRequestBean() {
    }

    public JoinedQueryRequestBean(EmployeeDTO emp) {
        this.emp=emp;
    }

    public EmployeeDTO getEmp() {
        return emp;
    }

    public void setEmp(EmployeeDTO emp) {
        this.emp = emp;
    }


}
