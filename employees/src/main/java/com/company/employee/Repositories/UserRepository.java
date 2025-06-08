package com.company.employee.Repositories;

import com.company.employee.Entity.UserProfileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findByUsername(String userName);
}

