package com.java.employee_yearly_job_details.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.util.ArrayList;

@Repository
public interface EmployeeYrJobDetailsRepository extends CrudRepository<EmployeesYrJobDetails, EmployeeYrRecordId> {
    ArrayList<EmployeesYrJobDetails> findAllByEmployeeID(int employeeID);

    ArrayList<EmployeesYrJobDetails> findAllByYear(Year year);

    ArrayList<EmployeesYrJobDetails> findAllByEmployeeIDAndYear(int employeeID, Year year);

    void deleteByEmployeeIDAndYear(int employeeID, Year year);
}
