package com.company.employee.service;

import com.company.employee.dto.BulkDeleteRequest;
import com.company.employee.dto.BulkUpdateRequest;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.OperationSummaryDTO;
import com.company.employee.entity.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    // Health check for code and database connection
    String testDatabaseConnection();

    // Fetching the whole page of employee data based on page and size
    Page<EmployeeDTO> fetchPagedDataList(int page, int size);

    // searching the employee data based on its employeeID
    EmployeeDTO searchDataBase(Long employeeId);

    // adding the employee data to database
    OperationSummaryDTO addDataToDataBase(List<EmployeeDTO> employeeDTOList);

    // updating the employee data based on its employeeID
    OperationSummaryDTO bulkUpdateDataToDataBase(List<BulkUpdateRequest> bulkUpdateRequestList);

    // deleting the employee data based on its employeeID/s
    OperationSummaryDTO bulkDeleteDataFromDataBase(List<Long> employeeIds);
}
