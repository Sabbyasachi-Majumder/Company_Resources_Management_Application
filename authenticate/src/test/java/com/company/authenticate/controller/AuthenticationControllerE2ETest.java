package com.company.authenticate.controller;

import com.company.authenticate.AuthenticateApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = AuthenticateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 9999)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthenticationControllerE2ETest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void testSuccessfulAuthentication() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"userName\": \"admin1\", \"password\": \"admin1\"}")
                .when()
                .post("/api/v1/authenticates/authenticate")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", containsString("Login successful for user admin1"))
                .body("data.token", notNullValue())
                .body("data.refreshToken", notNullValue());
    }

    @Test
    void testInvalidCredentials() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"userName\": \"testuser\", \"password\": \"wrongPassword\"}")
                .when()
                .post("/api/v1/authenticates/authenticate")
                .then()
                .statusCode(401)
                .body("status", equalTo("error"))
                .body("message", equalTo("Unauthorized: Invalid username or password [AUTH_401_INVALID_CREDENTIALS]"))
                .body("data", nullValue());
    }

    @Test
    void testMalformedRequest() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"userName\": \"\", \"password\": \"\"}")
                .when()
                .post("/api/v1/authenticates/authenticate")
                .then()
                .statusCode(400);
//                .body("status", equalTo("error"))
//                .body("message", containsString("Validation failed: userName: must not be blank; password: must not be blank"))
//                .body("data", nullValue());
    }

    @Test
    void testDisabledUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"userName\": \"disabledUser\", \"password\": \"validPassword\"}")
                .when()
                .post("/api/v1/authenticates/authenticate")
                .then()
                .statusCode(403)
                .body("status", equalTo("error"))
                .body("message", equalTo("Forbidden: User account is disabled [AUTH_403_DISABLED]"))
                .body("data", nullValue());
    }
}