package com.company.employee.service;

import com.company.employee.dto.BulkUpdateRequest;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.OperationSummaryDTO;

import java.util.List;

public interface EmployeeAdminService {

    // adding the employee data to database
    OperationSummaryDTO addDataToDataBase(List<EmployeeDTO> employeeDTOList);

    // updating the employee data based on its employeeID
    OperationSummaryDTO bulkUpdateDataToDataBase(List<BulkUpdateRequest> bulkUpdateRequestList);

    // deleting the employee data based on its employeeID/s
    OperationSummaryDTO bulkDeleteDataFromDataBase(List<Long> employeeIds);
}
