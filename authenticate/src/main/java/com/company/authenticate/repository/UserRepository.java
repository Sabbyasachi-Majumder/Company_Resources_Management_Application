package com.company.authenticate.repository;

import com.company.authenticate.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserProfileEntity, Long> {
    Optional<UserProfileEntity> findByUserName(String userName);

}

