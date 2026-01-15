//package com.company.employee.controllers.e2eTest;
//
//import com.company.employee.testUtils.TestUtils;
//import com.company.employee.dto.CreateEmployeesDTO;
//import com.company.employee.EmployeeApplication;
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import io.restassured.response.Response;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.Date;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.*;
//
//@SpringBootTest(classes = EmployeeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//@AutoConfigureWireMock(port = 0)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//public class EmployeeOperationsControllerE2ETest {
//
//    @LocalServerPort
//    private int port;
//
//    @BeforeEach
//    public void setup() {
//        RestAssured.baseURI = "http://localhost:" + port;
//    }
//
//    TestUtils testUtils;
//
//    //for authenticating and creating jwt tokens for each credential
//    private String authenticateAndGetToken(String userName, String password) {
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .body("{\"userName\": \"" + userName + "\", \"password\": \"" + password + "\"}")
//                .when()
//                .post("/api/v1/authenticates/authenticate")
//                .then()
//                .statusCode(200)
//                .extract()
//                .response();
//        return response.jsonPath().getString("data.token");
//    }
//
//
//    /* Test for GET /api/v1/employees/testConnection
//     * Purpose: Verify the connection establishment to the Employee Application
//     * Conditions:
//     * - Success: Returns 200 status code with success status and message "Connection to Employee Application is successfully established."
//     */
//    @Test
//    void testConnection_Success() {
//        given()
//                .contentType(ContentType.JSON)
//                .when()
//                .get("/api/v1/employees/testConnection")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", equalTo("Connection to Employee Application is successfully established."))
//                .body("data", nullValue());
//    }
//
//    /* Test for GET /api/v1/employees/testDataBaseConnection
//     * Purpose: Verify the database connection
//     * Conditions:
//     * - Success: Returns 200 status code with success status and non-null message from EmployeeService
//     */
//    @Test
//    void testDataBaseConnection_Success() {
//        given()
//                .contentType(ContentType.JSON)
//                .when()
//                .get("/api/v1/employees/testDataBaseConnection")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", equalTo("Connection from Employee Application to Employee Database successfully established."))
//                .body("data", nullValue());
//    }
//
//    /* Test for GET /api/v1/employees/fetchEmployees
//     * Purpose: Fetch paginated employee data with valid admin token
//     * Conditions:
//     * - Success: Returns 200 status code, success status, non-null data list with at least one employee
//     */
//    @Test
//    void fetchEmployees_Success_WithValidToken_Admin_DateOfBirth() {
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .queryParam("page", 1)
//                .queryParam("size", 10)
//                .when()
//                .get("/api/v1/employees/fetchEmployees")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", notNullValue())
//                .body("data", notNullValue())
//                .body("data.size()", greaterThanOrEqualTo(1))
//                .body("data[0].userName", anyOf(equalTo("admin1"), equalTo("disabledEmployee")));
//    }
//
//    /* Test for GET /api/v1/employees/fetchEmployees
//     * Purpose: Fetch paginated employee data with valid employee token
//     * Conditions:
//     * - Success: Returns 200 status code, success status, non-null data list with at least one employee
//     */
//    @Test
//    void fetchEmployees_Success_WithValidToken_Employee_DateOfBirth() {
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("user1", "user1"))
//                .queryParam("page", 1)
//                .queryParam("size", 10)
//                .when()
//                .get("/api/v1/employees/fetchEmployees")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", notNullValue())
//                .body("data", notNullValue())
//                .body("data.size()", greaterThanOrEqualTo(1))
//                .body("data[0].userName", anyOf(equalTo("admin1"), equalTo("disabledEmployee")));
//    }
//
//    /* Test for GET /api/v1/employees/fetchEmployees
//     * Purpose: Verify unauthorized access without token
//     * Conditions:
//     * - Error: Returns 401 status code with unauthorized error message
//     */
//    @Test
//    void fetchEmployees_Unauthorized_WithoutToken() {
//        given()
//                .contentType(ContentType.JSON)
//                .when()
//                .get("/api/v1/employees/fetchEmployees")
//                .then()
//                .statusCode(401)
//                .body("status", equalTo("error"))
//                .body("message", equalTo("Unauthorized: Authentication required [AUTH_401_NO_TOKEN]"))
//                .body("data", nullValue());
//    }
//
//    /* Test for GET /api/v1/employees/fetchEmployees
//     * Purpose: Verify behavior with invalid pagination parameters
//     * Conditions:
//     * - Error: Returns 400 status code with error message for invalid page/size
//     */
//    @Test
//    void fetchEmployees_InvalidPaginationParameters() {
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .queryParam("page", 0)
//                .queryParam("size", 0)
//                .when()
//                .get("/api/v1/employees/fetchEmployees")
//                .then()
//                .statusCode(400)
//                .body("status", equalTo("error"))
//                .body("message", containsString("Page index must not be less than zero"))
//                .body("data", nullValue());
//    }
//
//    /* Test for POST /api/v1/employees/addEmployees
//     * Purpose: Add employee with valid admin token
//     * Conditions:
//     * - Success: Returns 200 status code, success status, and confirms addition of one employee
//     */
//    @Test
//    void addEmployees_Success_WithValidTokenAndAdminDateOfBirth() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .body(sampleEmployeeRequest)
//                .when()
//                .post("/api/v1/employees/addEmployees")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", equalTo("Successfully added 1 . Add failed : 0"))
//                .body("data", notNullValue())
//                .body("data.apiResponse[0].status", equalTo("success"))
//                .body("data.apiResponse[0].message", equalTo("Successfully added Employee Id " + sampleEmployeeRequest.getEmpDetailsList().get(0).getEmployeeId() + " data records"));
//    }
//
//    /* Test for POST /api/v1/employees/addEmployees
//     * Purpose: Verify unauthorized access without token
//     * Conditions:
//     * - Error: Returns 401 status code with unauthorized error message
//     */
//    @Test
//    void addEmployees_Unauthorized_WithoutToken() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(sampleEmployeeRequest)
//                .when()
//                .post("/api/v1/employees/addEmployees")
//                .then()
//                .statusCode(401)
//                .body("status", equalTo("error"))
//                .body("message", equalTo("Unauthorized: Authentication required [AUTH_401_NO_TOKEN]"))
//                .body("data", nullValue());
//    }
//
//    /* Test for POST /api/v1/employees/addEmployees
//     * Purpose: Verify forbidden access with non-admin (employee) token
//     * Conditions:
//     * - Error: Returns 403 status code with insufficient permissions error message
//     */
//    @Test
//    void addEmployees_Forbidden_InsufficientPermissions() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("user1", "user1"))
//                .body(sampleEmployeeRequest)
//                .when()
//                .post("/api/v1/employees/addEmployees")
//                .then()
//                .statusCode(403)
//                .body("status", equalTo("error"))
//                .body("message", equalTo("Access Denied: Insufficient permissions [AUTH_403]"))
//                .body("data", nullValue());
//    }
//
//    /* Test for POST /api/v1/employees/addEmployees
//     * Purpose: Verify validation failure for employee data
//     * Conditions:
//     * - Error: Returns 422 status code with validation error messages
//     */
//    @Test
//    void addEmployees_ValidationFailure() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .body(sampleEmployeeRequest)
//                .when()
//                .post("/api/v1/employees/addEmployees")
//                .then()
//                .statusCode(422)
//                .body("status", equalTo("error"))
//                .body("message", allOf(
//                        containsString("userProfileList[0].userName: Employee Name must be at most 50 characters"),
//                        containsString("userProfileList[0].password: LastName must be at most 8 characters"),
//                        containsString("userProfileList[0].role: DateOfBirth must be admin or user")
//                ))
//                .body("data", nullValue());
//    }
//
//    /* Test for GET /api/v1/employees/searchEmployee/{employeeId}
//     * Purpose: Search employee by ID with valid admin token
//     * Conditions:
//     * - Success: Returns 200 status code, success status, and employee data for the given ID
//     */
//    @Test
//    void searchEmployee_Success_WithValidToken_Admin_DateOfBirth() {
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .when()
//                .get("/api/v1/employees/searchEmployee/1")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", notNullValue())
//                .body("data", notNullValue())
//                .body("data.userProfileList[0].userName", equalTo("admin1"));
//    }
//
//    /* Test for GET /api/v1/employees/searchEmployee/{employeeId}
//     * Purpose: Search employee by ID with valid employee token
//     * Conditions:
//     * - Success: Returns 200 status code, success status, and employee data for the given ID
//     */
//    @Test
//    void searchEmployee_Success_WithValidToken_Employee_DateOfBirth() {
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("user1", "user1"))
//                .when()
//                .get("/api/v1/employees/searchEmployee/1")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", notNullValue())
//                .body("data", notNullValue())
//                .body("data.userProfileList[0].userName", equalTo("admin1"));
//    }
//
//    /* Test for GET /api/v1/employees/searchEmployee/{employeeId}
//     * Purpose: Verify behavior when employee is not found
//     * Conditions:
//     * - Error: Returns 404 status code with resource not found error message
//     */
//    @Test
//    void searchEmployee_NotFound() {
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .when()
//                .get("/api/v1/employees/searchEmployee/999")
//                .then()
//                .statusCode(404)
//                .body("status", equalTo("error"))
//                .body("message", containsString("Resource not found"))
//                .body("data", nullValue());
//    }
//
//    /* Test for GET /api/v1/employees/searchEmployee/{employeeId}
//     * Purpose: Verify unauthorized access without token
//     * Conditions:
//     * - Error: Returns 401 status code with unauthorized error message
//     */
//    @Test
//    void searchEmployee_Unauthorized_WithoutToken() {
//        given()
//                .contentType(ContentType.JSON)
//                .when()
//                .get("/api/v1/employees/searchEmployee/1")
//                .then()
//                .statusCode(401)
//                .body("status", equalTo("error"))
//                .body("message", equalTo("Unauthorized: Authentication required [AUTH_401_NO_TOKEN]"))
//                .body("data", nullValue());
//    }
//
//    /* Test for PUT /api/v1/employees/updateEmployees
//     * Purpose: Update employee with valid admin token
//     * Conditions:
//     * - Success: Returns 200 status code, success status, and confirms update of one employee
//     * Note: Test uses /updateEmployees, but controller defines /updateEmployees; assuming correct endpoint is /updateEmployees
//     */
//    @Test
//    void updateEmployees_Success_WithValidTokenAndAdminDateOfBirth() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .body(sampleEmployeeRequest)
//                .when()
//                .put("/api/v1/employees/updateEmployees")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", notNullValue())
//                .body("data", notNullValue())
//                .body("data.apiResponse[0].status", equalTo("success"))
//                .body("data.apiResponse[0].message", equalTo("Successfully updated Employee Id " + sampleEmployeeRequest.getEmpDetailsList().get(0).getEmployeeId() + " data records"));
//    }
//
//    /* Test for PUT /api/v1/employees/updateEmployees
//     * Purpose: Verify behavior when employee to update is not found
//     * Conditions:
//     * - Success (partial): Returns 200 status code, but indicates update failure for non-existent employee
//     * Note: Test uses /updateEmployees, but controller defines /updateEmployees; assuming correct endpoint is /updateEmployees
//     */
//    @Test
//    void updateEmployees_NotFound() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .body(sampleEmployeeRequest)
//                .when()
//                .put("/api/v1/employees/updateEmployees")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", containsString("Update Success : 0 . Update Failed : 1"))
//                .body("data", notNullValue())
//                .body("data.apiResponse[0].status", equalTo("error"))
//                .body("data.apiResponse[0].message", equalTo("Employee Id " + sampleEmployeeRequest.getEmpDetailsList().get(0).getEmployeeId() + " doesn't exist"));
//    }
//
//    /* Test for PUT /api/v1/employees/updateEmployees
//     * Purpose: Verify unauthorized access without token
//     * Conditions:
//     * - Error: Returns 401 status code with unauthorized error message
//     * Note: Test uses /updateEmployees, but controller defines /updateEmployees; assuming correct endpoint is /updateEmployees
//     */
//    @Test
//    void updateEmployees_Unauthorized_WithoutToken() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(sampleEmployeeRequest)
//                .when()
//                .put("/api/v1/employees/updateEmployees")
//                .then()
//                .statusCode(401)
//                .body("status", equalTo("error"))
//                .body("message", equalTo("Unauthorized: Authentication required [AUTH_401_NO_TOKEN]"))
//                .body("data", nullValue());
//    }
//
//    /* Test for PUT /api/v1/employees/updateEmployees
//     * Purpose: Verify forbidden access with non-admin (employee) token
//     * Conditions:
//     * - Error: Returns 403 status code with insufficient permissions error message
//     * Note: Test uses /updateEmployees, but controller defines /updateEmployees; assuming correct endpoint is /updateEmployees
//     * Note: Test incorrectly uses POST method; should use PUT to match controller
//     */
//    @Test
//    void updateEmployees_Forbidden_InsufficientPermissions() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("user1", "user1"))
//                .body(sampleEmployeeRequest)
//                .when()
//                .post("/api/v1/employees/updateEmployees")
//                .then()
//                .statusCode(403)
//                .body("status", equalTo("error"))
//                .body("message", equalTo("Access Denied: Insufficient permissions [AUTH_403]"))
//                .body("data", nullValue());
//    }
//
//    /* Test for PUT /api/v1/employees/updateEmployees
//     * Purpose: Verify validation failure for employee data
//     * Conditions:
//     * - Error: Returns 422 status code with validation error messages
//     * Note: Test uses /updateEmployees, but controller defines /updateEmployees; assuming correct endpoint is /updateEmployees
//     * Note: Validation errors reference userProfileList, userName, password, and role, which are not in EmployeeFetchOrCreateDTO; assuming DTO mismatch or additional validation logic
//     */
//    @Test
//    void updateEmployees_ValidationFailure() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .body(sampleEmployeeRequest)
//                .when()
//                .put("/api/v1/employees/updateEmployees")
//                .then()
//                .statusCode(422)
//                .body("status", equalTo("error"))
//                .body("message", allOf(
//                        containsString("userProfileList[0].userName: Employee Name must be at most 50 characters"),
//                        containsString("userProfileList[0].password: LastName must be at most 8 characters"),
//                        containsString("userProfileList[0].role: DateOfBirth must be admin or user")
//                ))
//                .body("data", nullValue());
//    }
//
//    /* Test for DELETE /api/v1/employees/deleteEmployees
//     * Purpose: Delete employee with valid admin token
//     * Conditions:
//     * - Success: Returns 200 status code, success status, and confirms deletion of one employee
//     * Note: Test uses /deleteEmployees, but controller defines /deleteEmployees; assuming correct endpoint is /deleteEmployees
//     */
//    @Test
//    void deleteEmployees_Success_WithValidTokenAndAdminDateOfBirth() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .body(sampleEmployeeRequest)
//                .when()
//                .delete("/api/v1/employees/deleteEmployees")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", equalTo("Delete Success : 1. Delete Failed : " + (sampleEmployeeRequest.getEmpDetailsList().size() - 1)))
//                .body("data", notNullValue())
//                .body("data.apiResponse[0].status", equalTo("success"))
//                .body("data.apiResponse[0].message", equalTo("Successfully deleted Employee Id " + sampleEmployeeRequest.getEmpDetailsList().get(0).getEmployeeId() + " data records"));
//    }
//
//    /* Test for DELETE /api/v1/employees/deleteEmployees
//     * Purpose: Verify behavior when employee to delete is not found
//     * Conditions:
//     * - Success (partial): Returns 200 status code, but indicates deletion failure for non-existent employee
//     * Note: Test uses /deleteEmployees, but controller defines /deleteEmployees; assuming correct endpoint is /deleteEmployees
//     */
//    @Test
//    void deleteEmployees_NotFound() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        int deleteCounter = 0;
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("admin1", "admin1"))
//                .body(sampleEmployeeRequest)
//                .when()
//                .delete("/api/v1/employees/deleteEmployees")
//                .then()
//                .statusCode(200)
//                .body("status", equalTo("success"))
//                .body("message", containsString("Delete Success : " + deleteCounter + ". Delete Failed : " + (sampleEmployeeRequest.getEmpDetailsList().size() - deleteCounter)))
//                .body("data.apiResponse[0].status", equalTo("error"))
//                .body("data.apiResponse[0].message", equalTo("Employee Id " + sampleEmployeeRequest.getEmpDetailsList().get(0).getEmployeeId() + " doesn't exist"));
//    }
//
//    /* Test for DELETE /api/v1/employees/deleteEmployees
//     * Purpose: Verify unauthorized access without token
//     * Conditions:
//     * - Error: Returns 401 status code with unauthorized error message
//     * Note: Test uses /deleteEmployees, but controller defines /deleteEmployees; assuming correct endpoint is /deleteEmployees
//     */
//    @Test
//    void deleteEmployees_Unauthorized_WithoutToken() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(sampleEmployeeRequest)
//                .when()
//                .delete("/api/v1/employees/deleteEmployees")
//                .then()
//                .statusCode(401)
//                .body("status", equalTo("error"))
//                .body("message", equalTo("Unauthorized: Authentication required [AUTH_401_NO_TOKEN]"))
//                .body("data", nullValue());
//    }
//
//    /* Test for DELETE /api/v1/employees/deleteEmployees
//     * Purpose: Verify forbidden access with non-admin (employee) token
//     * Conditions:
//     * - Error: Returns 403 status code with insufficient permissions error message
//     * Note: Test uses /deleteEmployees, but controller defines /deleteEmployees; assuming correct endpoint is /deleteEmployees
//     * Note: Test incorrectly uses POST method; should use DELETE to match controller
//     */
//    @Test
//    void deleteEmployees_Forbidden_InsufficientPermissions() {
//        CreateEmployeesDTO sampleEmployeeRequest = testUtils.getSampleEmployeeRequest(1, "John", "Doe", new Date(631152000000L), "Male", 50000.0, new Date(1672531200000L), "L1", "Software Engineer", 2);
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + authenticateAndGetToken("user1", "user1"))
//                .body(sampleEmployeeRequest)
//                .when()
//                .post("/api/v1/employees/deleteEmployees")
//                .then()
//                .statusCode(403)
//                .body("status", equalTo("error"))
//                .body("message", equalTo("Access Denied: Insufficient permissions [AUTH_403]"))
//                .body("data", nullValue());
//    }
//}