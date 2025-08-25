package com.company.employee.repository;

import com.company.employee.dto.EmployeeResponseDTO;
import com.company.employee.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

    List<EmployeeResponseDTO> findByFirstName(String firstName);
}
