package com.company.authenticate.repository;

import com.company.authenticate.entity.UserProfileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findByUsername(String userName);
}

