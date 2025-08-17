package com.company.authenticate.controller.e2eTest;

import com.company.authenticate.AuthenticateApplication;
import com.company.authenticate.dto.UserProfileDTO;
import com.company.authenticate.dto.UserProfileRequestDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = AuthenticateApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserProfileControllerE2ETest {

    @LocalServerPort
    private int port;

    private String jwtToken;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
        jwtToken = authenticateAndGetToken("admin1", "admin1"); // Use admin1 with ADMIN
    }

    private String authenticateAndGetToken(String userName, String password) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"userName\": \"" + userName + "\", \"password\": \"" + password + "\"}")
                .when()
                .post("/api/v1/authenticates/authenticate")
                .then()
                .statusCode(200)
                .extract()
                .response();
        return response.jsonPath().getString("data.token");
    }

    // Test for GET /testConnection
    @Test
    void testConnection_Success() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/authenticates/testConnection")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", equalTo("Connection to User Application is successfully established."))
                .body("data", nullValue());
    }

    // Test for GET /testDataBaseConnection
    @Test
    void testDataBaseConnection_Success() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/authenticates/testDataBaseConnection")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", notNullValue())
                .body("data", nullValue());
    }

    // Tests for GET /fetchUsers
    @Test
    void fetchUsers_Success_WithValidToken() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .queryParam("page", 1)
                .queryParam("size", 10)
                .when()
                .get("/api/v1/authenticates/fetchUsers")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", notNullValue())
                .body("data", notNullValue())
                .body("data.size()", greaterThanOrEqualTo(1)) // At least admin1 and disabledUser from data.sql
                .body("data[0].userName", anyOf(equalTo("admin1"), equalTo("disabledUser")));
    }


    @Test
    void fetchUsers_Unauthorized_WithoutToken() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/authenticates/fetchUsers")
                .then()
                .statusCode(401)
                .body("status", equalTo("error"))
                .body("message", equalTo("Unauthorized: Authentication required [AUTH_401_NO_TOKEN]"))
                .body("data", nullValue());
    }

    @Test
    void fetchUsers_InvalidPaginationParameters() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .queryParam("page", 0) // Invalid page number
                .queryParam("size", 0) // Invalid size
                .when()
                .get("/api/v1/authenticates/fetchUsers")
                .then()
                .statusCode(400)
                .body("status", equalTo("error"))
                .body("message", containsString("Page index must not be less than zero"))
                .body("data", nullValue());
    }

    // Tests for POST /addUsers
    @Test
    void addUsers_Success_WithValidTokenAndAdminRole() {
        UserProfileDTO newUser = new UserProfileDTO();
        newUser.setUserId(5);
        newUser.setUserName("newUser");
        newUser.setPassword("newPassword");
        newUser.setRole("user");
        newUser.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(newUser)));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(request)
                .when()
                .post("/api/v1/authenticates/addUsers")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", equalTo("Successfully added 1 . Add failed : 0"))
                .body("data", notNullValue())
                .body("data.apiResponse[0].status", equalTo("success"))
                .body("data.apiResponse[0].message", equalTo("Successfully added User Id " + newUser.getUserId() + " data records"));
    }

    @Test
    void addUsers_Unauthorized_WithoutToken() {
        UserProfileDTO newUser = new UserProfileDTO();
        newUser.setUserId(5); // Avoid conflicts with existing IDs
        newUser.setUserName("newUser");
        newUser.setPassword("newPassword");
        newUser.setRole("user");
        newUser.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(newUser)));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/authenticates/addUsers")
                .then()
                .statusCode(401)
                .body("status", equalTo("error"))
                .body("message", equalTo("Unauthorized: Authentication required [AUTH_401_NO_TOKEN]"))
                .body("data", nullValue());
    }

    @Test
    void addUsers_Forbidden_InsufficientPermissions() {
        // Assuming admin1 has USER role, not ADMIN, as per data.sql
        UserProfileDTO newUser = new UserProfileDTO();
        newUser.setUserId(5);
        newUser.setUserName("newUser");
        newUser.setPassword("newPassword");
        newUser.setRole("user");
        newUser.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(newUser)));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authenticateAndGetToken("user1", "user1"))
                .body(request)
                .when()
                .post("/api/v1/authenticates/addUsers")
                .then()
                .statusCode(403)
                .body("status", equalTo("error"))
                .body("message", equalTo("Access Denied: Insufficient permissions [AUTH_403]"))
                .body("data", nullValue());
    }

    @Test
    void addUsers_ValidationFailure() {
        UserProfileDTO invalidUser = new UserProfileDTO();
        invalidUser.setUserId(5);
        invalidUser.setUserName(""); // Blank userName
        invalidUser.setPassword(""); // Blank password
        invalidUser.setRole(""); // Invalid role
        invalidUser.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(invalidUser)));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(request)
                .when()
                .post("/api/v1/authenticates/addUsers")
                .then()
                .statusCode(422)
                .body("status", equalTo("error"))
                .body("message", allOf(
                        containsString("userProfileList[0].userName: User Name must be at most 50 characters"),
                        containsString("userProfileList[0].password: Password must be at most 8 characters"),
                        containsString("userProfileList[0].role: Role must be admin or user")
                ))
                .body("data", nullValue());
    }

    // Tests for GET /searchUser/{USERId}
    @Test
    void searchUser_Success_WithValidTokenAndExistingUser() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/v1/authenticates/searchUser/1")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", notNullValue())
                .body("data", notNullValue())
                .body("data.userProfileList[0].userName", equalTo("admin1"));
    }

    @Test
    void searchUser_NotFound() {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .when()
                .get("/api/v1/authenticates/searchUser/999")
                .then()
                .statusCode(404)
                .body("status", equalTo("error"))
                .body("message", containsString("Resource not found"))
                .body("data", nullValue());
    }

    @Test
    void searchUser_Unauthorized_WithoutToken() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/v1/authenticates/searchUser/1")
                .then()
                .statusCode(401)
                .body("status", equalTo("error"))
                .body("message", equalTo("Unauthorized: Authentication required [AUTH_401_NO_TOKEN]"))
                .body("data", nullValue());
    }

    // Tests for PUT /updateUsers
    @Test
    void updateUsers_Success_WithValidTokenAndAdminRole() {
        UserProfileDTO updatedUser = new UserProfileDTO();
        updatedUser.setUserId(4);
        updatedUser.setUserName("admin1_updated");
        updatedUser.setPassword("admin12345678");
        updatedUser.setRole("admin");
        updatedUser.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(updatedUser)));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(request)
                .when()
                .put("/api/v1/authenticates/updateUsers")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", notNullValue())
                .body("data", notNullValue())
                .body("data.apiResponse[0].status", equalTo("success"))
                .body("data.apiResponse[0].message", equalTo("Successfully updated User Id " + updatedUser.getUserId() + " data records"));
    }

    @Test
    void updateUsers_NotFound() {
        UserProfileDTO updatedUser = new UserProfileDTO();
        updatedUser.setUserId(999); // Non-existent USER
        updatedUser.setUserName("nonexistent");
        updatedUser.setPassword("newPassword");
        updatedUser.setRole("user");
        updatedUser.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(updatedUser)));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(request)
                .when()
                .put("/api/v1/authenticates/updateUsers")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", containsString("Update Success : 0 . Update Failed : 1"))
                .body("data", notNullValue())
                .body("data.apiResponse[0].status", equalTo("error"))
                .body("data.apiResponse[0].message", equalTo("User Id " + updatedUser.getUserId() + " doesn't exist"));
    }

    @Test
    void updateUsers_Unauthorized_WithoutToken() {
        UserProfileDTO updatedUser = new UserProfileDTO();
        updatedUser.setUserId(4); // Use admin2 ID
        updatedUser.setUserName("admin2_updated");
        updatedUser.setPassword("newPassword");
        updatedUser.setRole("user");
        updatedUser.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(updatedUser)));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/api/v1/authenticates/updateUsers")
                .then()
                .statusCode(401)
                .body("status", equalTo("error"))
                .body("message", equalTo("Unauthorized: Authentication required [AUTH_401_NO_TOKEN]"))
                .body("data", nullValue());
    }

    @Test
    void updateUsers_Forbidden_InsufficientPermissions() {
        // Assuming admin1 has USER role, not ADMIN, as per data.sql
        UserProfileDTO newUser = new UserProfileDTO();
        newUser.setUserId(4);
        newUser.setUserName("updated_user_name");
        newUser.setPassword("updated_user_password");
        newUser.setRole("user");
        newUser.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(newUser)));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authenticateAndGetToken("user1", "user1"))
                .body(request)
                .when()
                .post("/api/v1/authenticates/updateUsers")
                .then()
                .statusCode(403)
                .body("status", equalTo("error"))
                .body("message", equalTo("Access Denied: Insufficient permissions [AUTH_403]"))
                .body("data", nullValue());
    }

    @Test
    void updateUsers_ValidationFailure() {
        UserProfileDTO invalidUser = new UserProfileDTO();
        invalidUser.setUserId(1);
        invalidUser.setUserName(""); // Blank userName
        invalidUser.setPassword(""); // Blank password
        invalidUser.setRole("INVALID_ROLE");
        invalidUser.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(invalidUser)));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(request)
                .when()
                .put("/api/v1/authenticates/updateUsers")
                .then()
                .statusCode(422)
                .body("status", equalTo("error"))
                .body("message", allOf(
                        containsString("userProfileList[0].userName: User Name must be at most 50 characters"),
                        containsString("userProfileList[0].password: Password must be at most 8 characters"),
                        containsString("userProfileList[0].role: Role must be admin or user")
                ))
                .body("data", nullValue());
    }

    // Tests for DELETE /deleteUsers for ADMIN with valid credentials and ADMIN role privilege
    @Test
    void deleteUsers_Success_WithValidTokenAndAdminRole() {
        // Note: This assumes admin1 has ADMIN role; adjust if admin1 is USER
        UserProfileDTO USERToDelete = new UserProfileDTO();
        USERToDelete.setUserId(1);
        USERToDelete.setUserName("testingAdmin");
        USERToDelete.setPassword("testingAdmin");
        USERToDelete.setRole("admin");
        USERToDelete.setEnabled(true);

        int deleteCounter = 1;

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(USERToDelete)));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(request)
                .when()
                .delete("/api/v1/authenticates/deleteUsers")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", equalTo("Delete Success : " + deleteCounter + ". Delete Failed : " + (request.getUserProfileList().size() - deleteCounter)))
                .body("data", notNullValue())
                .body("data.apiResponse[0].status", equalTo("success"))
                .body("data.apiResponse[0].message", equalTo("Successfully deleted User Id " + USERToDelete.getUserId() + " data records"));
    }

    @Test
    void deleteUsers_NotFound() {
        UserProfileDTO USERToDelete = new UserProfileDTO();
        USERToDelete.setUserId(999); // Non-existent USER
        USERToDelete.setUserName("testingUser");
        USERToDelete.setPassword("testingUser");
        USERToDelete.setRole("user");
        USERToDelete.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(USERToDelete)));

        int deleteCounter = 0;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .body(request)
                .when()
                .delete("/api/v1/authenticates/deleteUsers")
                .then()
                .statusCode(200)
                .body("status", equalTo("success"))
                .body("message", containsString("Delete Success : " + deleteCounter + ". Delete Failed : " + (request.getUserProfileList().size() - deleteCounter)))
                .body("data.apiResponse[0].status", equalTo("error"))
                .body("data.apiResponse[0].message", equalTo("User Id " + USERToDelete.getUserId() + " doesn't exist"));
    }

    @Test
    void deleteUsers_Unauthorized_WithoutToken() {
        UserProfileDTO USERToDelete = new UserProfileDTO();
        USERToDelete.setUserId(2);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(USERToDelete)));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .delete("/api/v1/authenticates/deleteUsers")
                .then()
                .statusCode(401)
                .body("status", equalTo("error"))
                .body("message", equalTo("Unauthorized: Authentication required [AUTH_401_NO_TOKEN]"))
                .body("data", nullValue());
    }

    @Test
    void deleteUsers_Forbidden_InsufficientPermissions() {
        // Assuming admin1 has USER role, not ADMIN, as per data.sql
        UserProfileDTO newUser = new UserProfileDTO();
        newUser.setUserId(4);
        newUser.setUserName("delete_user_name");
        newUser.setPassword("delete_user_password");
        newUser.setRole("user");
        newUser.setEnabled(true);

        UserProfileRequestDTO request = new UserProfileRequestDTO();
        request.setUserProfileList(new ArrayList<>(List.of(newUser)));

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + authenticateAndGetToken("user1", "user1"))
                .body(request)
                .when()
                .post("/api/v1/authenticates/deleteUsers")
                .then()
                .statusCode(403)
                .body("status", equalTo("error"))
                .body("message", equalTo("Access Denied: Insufficient permissions [AUTH_403]"))
                .body("data", nullValue());
    }
}