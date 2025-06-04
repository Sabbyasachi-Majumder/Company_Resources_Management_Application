package com.java.employee_yearly_job_details.service;

import com.java.employee_yearly_job_details.beans.EmployeeYrJobDetailsDetailBean;
import com.java.employee_yearly_job_details.repository.EmployeesYrJobDetails;
import com.java.employee_yearly_job_details.repository.EmployeeYrJobDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeYrJobDetailsServiceImpl implements EmployeeYrJobDetailsService {

    @Autowired
    public EmployeeYrJobDetailsRepository employeeYrJobDetailsRepository;

    //Adds Employee details to the employee table
    public void addData(ArrayList<EmployeesYrJobDetails> empList) {
        for (EmployeesYrJobDetails e : empList) {
            employeeYrJobDetailsRepository.save(e);
        }
    }

    public ArrayList<EmployeesYrJobDetails> searchOption(EmployeeYrJobDetailsDetailBean eyjBean) {
        if (eyjBean.getSearchFor().equalsIgnoreCase("employeeId") && eyjBean.getEyj().getEmployeeID() > 0)
            return findAllByEmployeeID(eyjBean.getEyj().getEmployeeID());
        else if (eyjBean.getSearchFor().equalsIgnoreCase("year") && (eyjBean.getEyj().getYear() != null)) {
            return findAllByYear(eyjBean.getEyj().getYear());
        } else if (eyjBean.getSearchFor().equalsIgnoreCase("both") && (eyjBean.getEyj().getYear() != null && eyjBean.getEyj().getEmployeeID() > 0)) {
            return findAllByEmployeeIDAndYear(eyjBean.getEyj().getEmployeeID(), eyjBean.getEyj().getYear());
        }
        else return null;
    }



    public ArrayList<EmployeesYrJobDetails> findAllByEmployeeID(int employeeID) {
        return employeeYrJobDetailsRepository.findAllByEmployeeID(employeeID);
    }

    public ArrayList<EmployeesYrJobDetails> findAllByYear(Year year) {
        return employeeYrJobDetailsRepository.findAllByYear(year);
    }

    public ArrayList<EmployeesYrJobDetails> findAllByEmployeeIDAndYear(int employeeID, Year year) {
        return employeeYrJobDetailsRepository.findAllByEmployeeIDAndYear(employeeID, year);
    }

    @Transactional
    public void delete(int employeeID, Year year) {
        employeeYrJobDetailsRepository.deleteByEmployeeIDAndYear(employeeID, year);
    }
}
