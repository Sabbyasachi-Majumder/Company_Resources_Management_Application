package com.java.employee_yearly_job_details.service;

import com.java.employee_yearly_job_details.beans.EmployeeYrJobDetailsDetailBean;
import com.java.employee_yearly_job_details.repository.EmployeesYrJobDetails;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

 interface EmployeeYrJobDetailsService {

     void addData(ArrayList<EmployeesYrJobDetails> empList);

     ArrayList<EmployeesYrJobDetails> searchOption(EmployeeYrJobDetailsDetailBean eyjBean);

     List<EmployeesYrJobDetails> findAllByEmployeeID(int employeeID) ;

    ArrayList<EmployeesYrJobDetails> findAllByYear(Year year);

    ArrayList<EmployeesYrJobDetails> findAllByEmployeeIDAndYear(int employeeID, Year year);

    void delete(int employeeID, Year year);

}
