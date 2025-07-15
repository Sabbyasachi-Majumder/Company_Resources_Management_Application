package com.company.department.repository;

import com.company.department.entity.DepartmentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<DepartmentEntity, Integer> {
}
