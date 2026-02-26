package com.company.employee.service;

import com.company.employee.dto.ColumnMetadata;
import com.company.employee.dto.EmployeeDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeUserService {

    // Returning the total no of employees count for the home page count
    Long countEntities();

    //Returning a list of metadata information of employee data table
    List<ColumnMetadata> getEmployeeColumnMetadata();

    // Fetching the whole page of employee data based on page and size
    Page<EmployeeDTO> fetchPagedDataList(int page, int size, String sortByColumnName, String sortOrder);


    // searching the employee data based on its employeeID
    EmployeeDTO searchDataBase(Long employeeId);
}
