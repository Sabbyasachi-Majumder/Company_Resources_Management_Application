package com.company.authenticate.repository.unitTest;

import com.company.authenticate.entity.UserProfileEntity;
import com.company.authenticate.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryUnitTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testUserRepositoryInjectionAndDataLoading() {
        assertNotNull(userRepository, "UserRepository should be injected");
        List<UserProfileEntity> users = userRepository.findAll();
        assertEquals(4, users.size(), "Should find 4 users from data.sql");
        assertTrue(users.stream().anyMatch(user -> "admin1".equals(user.getUserName())), "admin1 should be present");
        assertTrue(users.stream().anyMatch(user -> "disabledUser".equals(user.getUserName())), "disabledUser should be present");
    }
}