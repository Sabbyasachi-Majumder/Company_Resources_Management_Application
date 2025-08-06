package com.company.authenticate.service;

import com.company.authenticate.dto.ApiResponseDTO;
import com.company.authenticate.dto.UserProfileDTO;
import com.company.authenticate.dto.UserProfileResponseDTO;
import com.company.authenticate.entity.UserProfileEntity;
import com.company.authenticate.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService, UserDetailsService {

    private final UserRepository userRepository;
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;

    // For detailed logging in the application
    private static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    public UserProfileServiceImpl(UserRepository userRepository, DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserProfileDTO> debugFetchAllUsers() {
        if (userRepository == null) {
            logger.error("UserRepository is not initialized");
            throw new IllegalStateException("UserRepository is not initialized");
        }
        logger.debug("Fetching all users from UserProfileTable for debugging");
        List<UserProfileEntity> users = userRepository.findAll();
        logger.debug("Found {} users in UserProfileTable", users.size());
        users.forEach(user -> logger.debug("User: {}, enabled: {}, role: {}",
                user.getUserName(), user.isEnabled(), user.getRole()));
        return users.stream().map(this::toDTO).collect(Collectors.toList());
    }

    //Test Database Connection business logic
    public ApiResponseDTO<String> testDatabaseConnection() {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        try {
            if (connection.isValid(1)) {
                logger.debug("Testing successful . Database connection is present.");
                return new ApiResponseDTO<>("success", "Connection from User Application to User Database successfully established.", null);
            } else {
                logger.error("Testing failed . Database connection is not present.");
                return new ApiResponseDTO<>("error", "Connection to User Database failed to be established.", null);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Adds User details from the authenticate table
    public UserProfileDTO toDTO(UserProfileEntity entity) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(entity.getUserId());
        dto.setUserName(entity.getUserName());
        dto.setEnabled(entity.isEnabled());
        dto.setRole(entity.getRole());
        logger.debug("Mapped entity to DTO");
        return dto;
    }

    //Adds User table details to the authenticate details
    public UserProfileEntity toEntity(UserProfileDTO dto) {
        UserProfileEntity entity = new UserProfileEntity();
        entity.setUserId(dto.getUserId());
        entity.setUserName(dto.getUserName());
        entity.setPassword(passwordEncoder.encode(dto.getPassword())); //encoding the user password
        entity.setEnabled(dto.isEnabled());
        entity.setRole(dto.getRole());
        logger.debug("Mapped DTO to entity");
        return entity;
    }

    public ApiResponseDTO<List<UserProfileDTO>> fetchPagedDataList(Pageable pageable) {
        Page<UserProfileDTO> pagedData = fetchPageData(pageable);
        if (pageable.getPageNumber() < Math.ceil((float) pagedData.getTotalElements() / pageable.getPageSize())) {
            List<UserProfileDTO> currentData = pagedData.getContent();
            return new ApiResponseDTO<>("success", "Fetching page " + (pageable.getPageNumber() + 1) + " with " + currentData.size() + " User data records", currentData);
        } else
            return new ApiResponseDTO<>("success", "Total number of records is lower than the current page number " + (pageable.getPageNumber() + 1) + " containing " + pageable.getPageSize() + " User data records each page.", null);

    }

    // Fetches all data with pagination
    @Override
    public Page<UserProfileDTO> fetchPageData(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::toDTO);
    }

    // Business logic to add authenticate data records one by one .
    public ApiResponseDTO<UserProfileResponseDTO> addDataToDataBase(ArrayList<UserProfileDTO> empList) {
        ArrayList<ApiResponseDTO<UserProfileResponseDTO>> responses = new ArrayList<>();
        long addCounter = 0;
        for (UserProfileDTO e : empList) {
            if (!userRepository.existsById(e.getUserId())) {
                logger.debug("Adding userId {} ", e.getUserId());
                addData(toEntity(e));
                addCounter++;
                responses.add(new ApiResponseDTO<>("success", "Successfully added User Id " + e.getUserId() + " data records", null));
            } else {
                logger.error("userId {} is already present thus not added again.", e.getUserId());
                responses.add(new ApiResponseDTO<>("error", "User Id " + e.getUserId() + " already exists ", null));
            }
        }
        return new ApiResponseDTO<>("success", "Successfully added " + addCounter + " . Add failed : " + (empList.size() - addCounter), new UserProfileResponseDTO(null, responses));
    }

    //Adds user details to the userprofile table
    @Transactional
    @Override
    public void addData(UserProfileEntity entity) {
        logger.debug("Attempting to add userId {}", entity.getUserId());
        userRepository.save(entity);
        logger.debug("Added userId {} successfully", entity.getUserId());
    }

    // Business logic to search database for a user profile based on its userId
    public ApiResponseDTO<UserProfileResponseDTO> searchDataBase(long userId) {
        ArrayList<UserProfileDTO> entityArrayList = new ArrayList<>();
        entityArrayList.add(toDTO(searchData(userId)));
        return new ApiResponseDTO<>("success", "Successfully found User Id " + userId + " data records", new UserProfileResponseDTO(entityArrayList, null));
    }

    // Calling findById to search the table for a user based on userId
    @Override
    public UserProfileEntity searchData(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("userId " + userId + " not found"));
    }

    @Transactional
    public ApiResponseDTO<UserProfileResponseDTO> updateDataToDataBase(ArrayList<UserProfileDTO> empList) {
        ArrayList<ApiResponseDTO<UserProfileResponseDTO>> responses = new ArrayList<>();
        long updateCounter = 0;
        for (UserProfileDTO e : empList) {
            if (userRepository.existsById(e.getUserId())) {
                logger.debug("Updated userId {} successfully", e.getUserId());
                userRepository.save(toEntity(e));
                updateCounter++;
                responses.add(new ApiResponseDTO<>("success", "Successfully updated User Id " + e.getUserId() + " data records", null));
            } else {
                logger.error("Updating userId {} failed since userId doesn't exist", e.getUserId());
                responses.add(new ApiResponseDTO<>("error", "User Id " + e.getUserId() + " doesn't exist", null));
            }
        }
        return new ApiResponseDTO<>("success", "Update Success : " + updateCounter + " . Update Failed : " + (empList.size() - updateCounter), new UserProfileResponseDTO(null, responses));
    }

    @Transactional
    public ApiResponseDTO<UserProfileResponseDTO> deleteDataFromDataBase(ArrayList<UserProfileDTO> empList) {
        ArrayList<ApiResponseDTO<UserProfileResponseDTO>> responses = new ArrayList<>();
        long deleteCounter = 0;
        for (UserProfileDTO e : empList) {
            ApiResponseDTO<UserProfileResponseDTO> apiResponse;
            if (userRepository.existsById(e.getUserId())) {
                logger.debug("Deleted userId {} successfully", e.getUserId());
                userRepository.deleteById(e.getUserId());
                deleteCounter++;
                apiResponse = new ApiResponseDTO<>("success", "Successfully deleted User Id " + e.getUserId() + " data records", null);
            } else {
                logger.error("Deleting userId {} failed since userId doesn't exist", e.getUserId());
                apiResponse = new ApiResponseDTO<>("error", "User Id " + e.getUserId() + " doesn't exist", null);
            }
            responses.add(apiResponse);
        }
        return new ApiResponseDTO<>("success", "Delete Success : " + deleteCounter + ". Delete Failed : " + (empList.size() - deleteCounter), new UserProfileResponseDTO(null, responses));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by username: {}", username);
        if (username == null || username.trim().isEmpty()) {
            logger.error("Username is null or empty");
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }
        if (userRepository == null) {
            logger.error("UserRepository is not initialized");
            throw new IllegalStateException("UserRepository is not initialized");
        }
        UserProfileEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> {
                    logger.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        logger.debug("Found user: {}, enabled: {}, role: {}", user.getUserName(), user.isEnabled(), user.getRole());
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities(user.getRole())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();

        logger.info("Successfully loaded user: {}, roles: {}", username, userDetails.getAuthorities());
        return userDetails;
    }
}
