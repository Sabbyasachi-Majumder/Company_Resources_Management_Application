package com.company.api_gateway;

import com.company.api_gateway.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class ApiGatewayApplicationTests {

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    void contextLoads() {
    }
}