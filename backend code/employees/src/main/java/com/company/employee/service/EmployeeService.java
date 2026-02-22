package com.company.employee.service;

import com.company.employee.dto.BulkUpdateRequest;
import com.company.employee.dto.EmployeeDTO;
import com.company.employee.dto.OperationSummaryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    // Fetching the whole page of employee data based on page and size
    Page<EmployeeDTO> fetchPagedDataList(int page, int size, String sortByColumnName, String sortOrder);

    Long countEntities();

    // searching the employee data based on its employeeID
    EmployeeDTO searchDataBase(Long employeeId);

    // adding the employee data to database
    OperationSummaryDTO addDataToDataBase(List<EmployeeDTO> employeeDTOList);

    // updating the employee data based on its employeeID
    OperationSummaryDTO bulkUpdateDataToDataBase(List<BulkUpdateRequest> bulkUpdateRequestList);

    // deleting the employee data based on its employeeID/s
    OperationSummaryDTO bulkDeleteDataFromDataBase(List<Long> employeeIds);
}
