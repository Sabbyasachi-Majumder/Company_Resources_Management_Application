package com.company.authenticate.service;

import com.company.authenticate.entity.UserProfileEntity;
import com.company.authenticate.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticateServiceImpl.class);

    private final UserRepository userRepository;

    public AuthenticateServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading user by username: {}", username);
        UserProfileEntity user = userRepository.findByUserName(username)
                .orElseThrow(() -> {
                    logger.error("User not found with username: {}", username);
                    return new UsernameNotFoundException("User not found with username: " + username);
                });

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities(user.getRole())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();

        logger.info("Loaded user: {}, roles: {}", username, userDetails.getAuthorities());
        return userDetails;
    }
}
