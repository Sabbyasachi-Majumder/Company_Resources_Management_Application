# Company Resources Management Application

A microservices-based application built with Spring Boot and Maven, designed to manage company resources efficiently
through independent services. It includes an API Gateway for request routing and security, a Service Registry for
dynamic service discovery, and microservices for authentication, employee, project, and department management. This
project showcases a scalable, secure, and modern architecture suitable for enterprise applications.

## Project Overview

The **Company Resources Management Application** is a modular, microservices-based system designed to handle core
business operations such as employee management, project tracking, and department administration. It leverages Spring
Boot for rapid development, Spring Cloud for microservices orchestration, and JWT-based authentication for secure
access. The application is built with scalability, maintainability, and industry-standard practices in mind, making it a
robust demonstration of modern Java-based enterprise solutions.

Key features include:

- **API Gateway**: Routes requests and enforces authentication/authorization using Spring Cloud Gateway.
- **Service Registry**: Uses Netflix Eureka for dynamic service discovery.
- **Authenticate Service**: Manages user authentication and generates JWT tokens.
- **Employee Service**: Handles CRUD operations for employee data using MySQL.
- **Project Service**: Manages project-related data with MySQL as the backend.
- **Department Service**: Performs CRUD operations for department data using MongoDB.
- **Robust Testing Framework**: Includes unit tests across all services (Authenticate, Employee, Project,
  Department) and end-to-end testing for authenticate service presently, utilizing H2 in-memory database (for SQL-based
  services), Embedded MongoDB (for Department Service),
  Mockito, RestAssured, WireMock, and @DirtiesContext to ensure reliable, isolated, and comprehensive test execution.

## Technologies Used

- **Java**: Version 17
- **Spring Boot**: 3.5.0 (3.5.3 for Authenticate Service, 3.3.4 for Service Registry)
- **Spring Cloud**:
    - Spring Cloud Gateway (API Gateway)
    - Netflix Eureka (Service Registry)
- **Databases**:
    - MySQL (version 8.0 or higher) for Authenticate, Employee, and Project Services
    - MongoDB (version 4.0 or higher) for Department Service
    - **H2 Database** (in-memory) for integration and end-to-end testing in the Authenticate Service , Employee Service
      and Project Services
    - **Embedded MongoDB** for unit and integration testing in Department Service
- **Security**:
    - Spring Security for authentication and authorization
    - JWT (JSON Web Tokens) via `jjwt` library (version 0.12.6)
    - **BCryptPasswordEncoder** utility for generating hashed passwords for test data
- **API Documentation**: Springdoc OpenAPI (version 2.7.0) for Swagger UI
- **Build Tool**: Maven (multi-module project)
- **Dependencies**:
    - Spring Data JPA (Employee, Project, Authenticate Services)
    - Spring Data MongoDB (Department Service)
    - Spring Boot Actuator for monitoring
    - Lombok (version 1.18.34/1.18.38) for boilerplate reduction
    - Logback (version 1.5.13) and SLF4J (version 2.0.16) for logging
    - MySQL Connector (runtime) for MySQL database connectivity
    - Spring Boot DevTools for development-time enhancements
- **Testing**:
    - **JUnit 5** and **Mockito** for unit testing service and repository layers
    - **Spring Security Test** for testing authentication and authorization logic
    - **RestAssured** for end-to-end API testing of REST endpoints
    - **WireMock** for mocking external services during E2E testing
    - **Spring Test** with `@DirtiesContext` for consistent test context management
    - **H2 Database** with `schema.sql` and `data.sql` for isolated test environments
    - **BCryptPasswordEncoder** utility for generating hashed passwords for test data

## Prerequisites

To set up and run the project, ensure you have the following installed:

- **Java**: JDK 17
- **Maven**: Version 3.6.0 or higher
- **IDE**: IntelliJ IDEA, Eclipse, or any IDE with Spring Boot support
- **Databases**:
    - MySQL (version 8.0 or higher) for Authenticate, Employee, and Project Services
    - MongoDB (version 4.0 or higher) for Department Service
- **Git**: For cloning the repository
- **Optional**: Docker (for future containerization enhancements)

## Project Structure

The project follows a Maven multi-module structure with the following modules:

- **api-gateway**: Routes requests to microservices using Spring Cloud Gateway and enforces JWT-based
  authentication/authorization.
- **service-registry**: Implements Netflix Eureka Server for dynamic service discovery.
- **authenticate**: Handles user authentication, JWT token generation, and user CRUD operations using MySQL, with
  extensive unit and E2E tests.
- **employees-service**: Manages employee-related CRUD operations with Spring Data JPA and MySQL, including unit tests
  for service classes.
- **projects-service**: Manages project-related CRUD operations with Spring Data JPA and MySQL, including unit tests for
  service classes.
- **departments-service**: Manages department-related CRUD operations with Spring Data MongoDB, including unit tests for
  service classes.

Each module is independently deployable and communicates via REST APIs, with the API Gateway serving as the entry point.

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/<your-username>/Company_Resources_Management_Application.git
   ```
2. Navigate to the project root:
   ```bash
   cd Company_Resources_Management_Application
   ```
3. Build the project using Maven with the custom settings file:
   ```bash
   mvn clean install --s"company_resources_management_application_settings.xml"
   ```
4. Run each module independently:
    - **API Gateway**:
      ```bash
      cd api-gateway
      mvn spring-boot:run
      ```
    - **Employees Service**:
      ```bash
      cd employees-service
      mvn spring-boot:run
      ```
    - **Projects Service**:
      ```bash
      cd projects-service
      mvn spring-boot:run
      ```
    - **Departments Service**:
      ```bash
      cd departments-service
      mvn spring-boot:run
      ```

## Accessing the Application

- Service Registry Dashboard: http://localhost:8761 (Eureka Server)
  API Gateway: http://localhost:8080
- Authenticate Service: Accessible via Gateway at /authenticate (e.g., http://localhost:8080/authenticate)
- Employees Service: Accessible via Gateway at /employees (e.g., http://localhost:8080/employees)
- Projects Service: Accessible via Gateway at /projects (e.g., http://localhost:8080/projects)
- Departments Service: Accessible via Gateway at /departments (e.g., http://localhost:8080/departments)
- Swagger UI (API Documentation):
    - Authenticate Service: http://localhost:8080/authenticate/swagger-ui.html
    - Employees Service: http://localhost:8080/employees/swagger-ui.html
    - Projects Service: http://localhost:8080/projects/swagger-ui.html
    - Departments Service: http://localhost:8080/departments/swagger-ui.html
    - Actuator Endpoints (for monitoring):
    - Health: http://localhost:<service-port>/actuator/health
    - Metrics: http://localhost:<service-port>/actuator/metrics
    - Info: http://localhost:<service-port>/actuator/info

## Testing

The application incorporates a robust testing framework across all core services (Authenticate, Employee, Project, and
Department), ensuring high code quality, reliability, and maintainability. Tests are implemented using industry-standard
tools and practices, focusing on unit tests for service classes, repositories, and utilities, as well as end-to-end (
E2E) tests for API endpoints. This comprehensive approach covers success scenarios, error handling, edge cases, and
security validations.

## Postman Testing Setup

1. Import collections from `/postman/collections/` into Postman.
2. Import environments from `/postman/environments/`.
3. Start all services (API gateway on port 8080, etc.).
4. Run collections to test CRUD for authenticate (MySQL), employee (MySQL), project (MySQL), department (MongoDB).

### Authenticate Service Testing

#### Unit Tests

- **Purpose**: Validate individual components (services and repositories) in isolation.
- **Components Tested**:
    - **AuthenticateServiceImpl**: Tests authentication logic, JWT token generation, and error handling (e.g., invalid
      credentials, disabled accounts, null inputs) using Mockito to mock AuthenticationManager and JwtUtil.
    - **UserProfileServiceImpl**: Tests user profile management, database connection checks, entity-DTO mapping, and
      UserDetails loading for Spring Security, with mocked UserRepository, DataSource, and PasswordEncoder.
    - **UserRepository**: Verifies repository injection and data loading from data.sql using Spring Boot Test and H2
      in-memory database.
- **Key Features**:
    - Comprehensive test cases for success and failure scenarios, including BadCredentialsException, DisabledException,
      and UsernameNotFoundException.
    - Mocking of dependencies to isolate unit tests and ensure repeatable, deterministic results.
    - Validation of response structures (ApiResponseDTO, AuthResponseDTO, UserProfileResponseDTO) and error messages for
      consistent API behavior.

#### End-to-End (E2E) Tests

- **Purpose**: Validate the full HTTP request-response cycle for Authenticate Service endpoints.
- **Endpoints Tested**:
    - `/api/v1/authenticates/authenticate`: Tests successful authentication, invalid credentials, malformed requests,
      and disabled user accounts.
    - `/api/v1/authenticates/testConnection`: Verifies service connectivity.
    - `/api/v1/authenticates/testDataBaseConnection`: Validates database connectivity.
    - `/api/v1/authenticates/fetchUsers`: Tests paginated user retrieval with authorization checks.
    - `/api/v1/authenticates/addUsers`, `/updateUsers`, `/deleteUsers`: Tests CRUD operations with admin role checks,
      validation failures, and non-existent user scenarios.
- **Key Features**:
    - Uses RestAssured for simulating HTTP requests and validating responses (status codes, JSON structure, error
      messages).
    - Leverages H2 in-memory database with schema.sql and data.sql for isolated and repeatable test environments.
    - Employs WireMock to mock external service interactions, ensuring test reliability.
    - Uses @DirtiesContext to reset the Spring application context before each test, preventing state leakage and
      ensuring consistent test execution during Maven builds.
    - Tests authentication with dynamically generated JWT tokens and validates role-based access control (e.g.,
      admin-only operations).
    - Covers edge cases like unauthorized access (401), invalid inputs (422), forbidden actions (403), and invalid
      pagination parameters (400).

#### Test Data Setup

- **PasswordHashGenerator**: A utility class that uses BCryptPasswordEncoder to generate hashed passwords for test
  users (admin1, disabledUser, user1, admin2), ensuring secure and consistent test data.
- **Data Loading**: Uses data.sql to preload test users into the H2 database, verified by UserRepositoryUnitTest to
  ensure correct initialization (e.g., 4 users with specific usernames).

### Employee Service Testing

#### Unit Tests

- **Purpose**: Validate service layer logic for employee management in isolation.
- **Components Tested**:
    - **EmployeeServiceImpl**: Tests CRUD operations (create, read, update, delete) for employee entities, including
      validation of inputs, entity-DTO mapping, and error handling (e.g., duplicate entries, non-existent IDs, invalid
      data formats) using Mockito to mock EmployeeRepository and Validator.
    - **EmployeeRepository**: Verifies data persistence and query methods using Spring Boot Test and H2 in-memory
      database, including tests for saving, finding by ID, and custom queries.
- **Key Features**:
    - Covers success paths (e.g., creating a new employee) and failure scenarios (e.g., EntityNotFoundException,
      constraint violations).
    - Mocking ensures isolation from external dependencies like databases or other services.
    - Assertions on response DTOs and exception messages for API consistency.

### Project Service Testing

#### Unit Tests

- **Purpose**: Validate service layer logic for project management in isolation.
- **Components Tested**:
    - **ProjectServiceImpl**: Tests CRUD operations for project entities, including assignment to employees/departments,
      status updates, and error handling (e.g., invalid project states, missing references) using Mockito to mock
      ProjectRepository and related services.
    - **ProjectRepository**: Tests repository methods for persistence and retrieval using Spring Boot Test and H2
      in-memory database, covering scenarios like finding projects by status or ID.
- **Key Features**:
    - Comprehensive coverage of business logic, such as project lifecycle validation.
    - Isolation through mocking, with focus on repeatable tests and edge cases like concurrent updates.

### Department Service Testing

#### Unit Tests

- **Purpose**: Validate service layer logic for department management in isolation.
- **Components Tested**:
    - **DepartmentServiceImpl**: Tests CRUD operations for department documents, including hierarchical structures (
      e.g., sub-departments), validation, and error handling (e.g., duplicate names, invalid hierarchies) using Mockito
      to mock DepartmentRepository.
    - **DepartmentRepository**: Verifies MongoDB-specific operations like saving, finding by name, and aggregation
      queries using Spring Boot Test and Embedded MongoDB for in-memory testing.
- **Key Features**:
    - Handles NoSQL-specific scenarios, such as flexible schema validation and query optimization.
    - Mocking isolates tests from actual MongoDB instances, ensuring fast and reliable execution.
    - Focus on edge cases like large document sizes or query performance.

### Testing Best Practices

- **Isolation**: Unit tests mock all dependencies, and integration tests use in-memory databases (H2 for SQL, Embedded
  MongoDB for NoSQL) with @DirtiesContext to prevent state leakage.
- **Comprehensive Coverage**: Tests include happy paths, error conditions, edge cases, and security validations, aiming
  for high code coverage (e.g., >80%).
- **Maintainability**: Descriptive test names, structured assertions, and comments for readability.
- **Industry Standards**: Aligns with Spring Boot testing guidelines, using JUnit 5, Mockito, RestAssured, Spring
  Security Test, and in-memory databases for efficiency.

## Unique Features

- **Centralized Routing**: Spring Cloud Gateway provides dynamic routing and load balancing, ensuring a single entry
  point for all client requests.
- **Service Discovery**: Netflix Eureka enables dynamic registration and discovery of microservices, improving
  scalability and fault tolerance.
- **Secure Authentication**: JWT-based authentication with configurable token expiration (36,000 seconds for access
  tokens, 604,800 seconds for refresh tokens) ensures secure access.
- **Heterogeneous Databases**: Supports MySQL (Employee, Project, Authenticate) and MongoDB (Department), showcasing
  versatility in data management.
- **API Documentation**: Springdoc OpenAPI provides interactive Swagger UI with sorted operations and tags for each
  service.
- **Monitoring**: Spring Boot Actuator exposes health, metrics, and info endpoints for real-time service monitoring.
- **Dynamic Port Assignment**: Using server.port: 0 with Eureka ensures flexible and conflict-free port allocation.
- **Robust Testing**: Comprehensive unit and E2E tests across all services, covering CRUD operations, authentication,
  and edge cases, with isolated environments using H2, Embedded MongoDB, and WireMock.

## Contribution Guidelines

[Placeholder: Guidelines for contributing to the project, including coding standards and pull request processes, will be added here.]

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Future Enhancements

- Containerization: Add Docker and Docker Compose support for easier deployment and orchestration.
- Centralized Configuration: Implement Spring Cloud Config Server for managing configuration across services.
- Circuit Breaker: Integrate Resilience4j for fault tolerance in inter-service communication.
- Distributed Tracing: Add Spring Cloud Sleuth and Zipkin for request tracing across microservices.
- CI/CD Pipeline: Set up GitHub Actions for automated testing and deployment.
- Message Queue: Introduce RabbitMQ or Kafka for asynchronous communication.
  Database Optimization: Add connection pooling (e.g., HikariCP) and indexing for MySQL/MongoDB.
- **Enhanced Testing**: Introduce Test containers for containerized database testing to further isolate and standardize
  test environments across all services.

## Contact

For questions or feedback, please contact sabbyasachi.majumder.official@gmail.com.