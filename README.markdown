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
- **Robust Testing Framework**: End-to-end tests in the Authenticate Service use H2 in-memory database with `@DirtiesContext` to ensure consistent and reliable test execution, preventing issues like table creation conflicts during Maven builds.

## Technologies Used

- **Java**: Version 17
- **Spring Boot**: 3.5.0 (3.5.3 for Authenticate Service, 3.3.4 for Service Registry)
- **Spring Cloud**:
    - Spring Cloud Gateway (API Gateway)
    - Netflix Eureka (Service Registry)
- **Databases**:
    - MySQL (version 8.0 or higher) for Authenticate, Employee, and Project Services
    - MongoDB (version 4.0 or higher) for Department Service
    - **H2 Database** (in-memory) for integration and end-to-end testing in the Authenticate Service
- **Security**:
    - Spring Security for authentication and authorization
    - JWT (JSON Web Tokens) via `jjwt` library (version 0.12.6)
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
  - **Testing**: JUnit, Mockito, Spring Security Test, **Spring Test with `@DirtiesContext` for consistent test context management**, RestAssured for end-to-end API testing, WireMock for mocking external services

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
- **authenticate**: Handles user authentication, JWT token generation, and user CRUD operations using MySQL.
- **employees-service**: Manages employee-related CRUD operations with Spring Data JPA and MySQL.
- **projects-service**: Manages project-related CRUD operations with Spring Data JPA and MySQL.
- **departments-service**: Manages department-related CRUD operations with Spring Data MongoDB.

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

## Unique Features

- **Centralized Routing** : Spring Cloud Gateway provides dynamic routing and load balancing, ensuring a single entry
  point for all client requests.
- **Service Discovery** : Netflix Eureka enables dynamic registration and discovery of microservices, improving
  scalability and fault tolerance.
- **Secure Authentication** : JWT-based authentication with configurable token expiration (36,000 seconds for access
  tokens, 604,800 seconds for refresh tokens) ensures secure access.
- **Heterogeneous Databases** : Supports MySQL (Employee, Project, Authenticate) and MongoDB (Department), showcasing
  versatility in data management.
- **API Documentation** : Springdoc OpenAPI provides interactive Swagger UI with sorted operations and tags for each
  service.
- **Monitoring** : Spring Boot Actuator exposes health, metrics, and info endpoints for real-time service monitoring.
- **Dynamic Port Assignment** : Using server.port: 0 with Eureka ensures flexible and conflict-free port allocation.

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
- **Enhanced Testing**: Introduce Test containers for containerized database testing to further isolate and standardize test environments across all services.

## Contact

For questions or feedback, please contact sabbyasachi.majumder.official@gmail.com.