package com.company.employee.Configs;

import com.company.employee.Entity.RoleEntity;
import com.company.employee.Entity.UserProfileEntity;
import com.company.employee.Repositories.RoleRepository;
import com.company.employee.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles
        RoleEntity userRoleEntity = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    RoleEntity roleEntity = new RoleEntity("ROLE_USER");
                    logger.info("Creating roleEntity : {}", roleEntity.getName());
                    return roleRepository.save(roleEntity);
                });

        RoleEntity adminRoleEntity = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    RoleEntity roleEntity = new RoleEntity("ROLE_ADMIN");
                    logger.info("Creating roleEntity: {}", roleEntity.getName());
                    return roleRepository.save(roleEntity);
                });

        // Initialize users
        if (userRepository.findByUsername("user").isEmpty()) {
            UserProfileEntity user = new UserProfileEntity("user", passwordEncoder.encode("userPass"), true);
            user.addRole(userRoleEntity);
            userRepository.save(user);
            logger.info("Created user: {}, roles: {}", user.getUsername(), user.getRoleEntities().stream().map(RoleEntity::getName).toList());
        }

        if (userRepository.findByUsername("admin").isEmpty()) {
            UserProfileEntity admin = new UserProfileEntity("admin", passwordEncoder.encode("adminPass"), true);
            admin.addRole(userRoleEntity);
            admin.addRole(adminRoleEntity);
            userRepository.save(admin);
            logger.info("Created user: {}, roles: {}", admin.getUsername(), admin.getRoleEntities().stream().map(RoleEntity::getName).toList());
        }
    }
}