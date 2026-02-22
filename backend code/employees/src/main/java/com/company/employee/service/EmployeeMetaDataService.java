package com.company.employee.service;

import com.company.employee.dto.ColumnMetadata;

import java.util.List;

public interface EmployeeMetaDataService {

    //Returning a list of metadata information of employee data table
    List<ColumnMetadata> getEmployeeColumnMetadata();
}
