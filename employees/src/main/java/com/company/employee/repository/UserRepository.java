package com.company.employee.repository;

import com.company.employee.entity.UserProfileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findByUsername(String userName);
}

