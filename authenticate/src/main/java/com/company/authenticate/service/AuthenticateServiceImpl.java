package com.company.authenticate.service;

import com.company.authenticate.dto.ApiResponseDTO;
import com.company.authenticate.dto.AuthResponseDTO;
import com.company.authenticate.repository.UserRepository;
import com.company.authenticate.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticateServiceImpl.class);

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticateServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public ApiResponseDTO<AuthResponseDTO> authenticateUser(String userName, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, password));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        return new ApiResponseDTO<>("success",
                "Login successful for user " + userName, new AuthResponseDTO(accessToken, refreshToken));
    }
}
