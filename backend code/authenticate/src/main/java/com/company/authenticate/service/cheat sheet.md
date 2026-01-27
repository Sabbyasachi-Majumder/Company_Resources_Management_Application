# Project Cheat-Sheet - Authentication & User Profile Module

## Quick Overview
A Spring Boot module implementing user authentication with JWT and full CRUD-based user profile management with pagination and database validation.

## File Structure Summary
AuthenticateServiceImpl.java → Handles authentication, JWT token generation, and login response formatting.  
UserProfileService.java → Interface defining user profile operations, conversion, CRUD, pagination, and authentication loading.  
UserProfileServiceImpl.java → Implements CRUD logic, DTO/entity mapping, pagination, database connection test, and Spring Security’s UserDetailsService.

## Core Technologies & Frameworks Used
Spring Boot → Backend framework to build REST services.  
Spring Security → Provides authentication and role-based authorization.  
Spring Data JPA → Simplifies DB access with repositories and pagination.  
JWT (Json Web Token) → Generates access and refresh tokens for authentication.  
SLF4J Logging → Structured logging for debug and error tracking.  
DataSource & JDBC Utils → Direct DB connection validation.

## All Java / Spring Annotations Used
@Service → Marks class as a Spring-managed service component.  
@Transactional → Wraps methods in database transactions.  
@Override → Indicates method overrides interface or parent definition.

## Key Concepts & Techniques Demonstrated
DTO ↔ Entity Mapping → Converting DB entities to transport-friendly DTOs.  
Pagination with Page & Pageable → Efficient paginated DB retrieval.  
JWT Token Generation → Creating secure access & refresh tokens.  
UserDetailsService Implementation → Custom authentication loading for Spring Security.  
Password Encoding → Ensures secure stored passwords.  
Exception Handling & Validation → Ensures user and DB errors are caught cleanly.

## Important Patterns & Architectures
Service Layer Pattern → Business logic isolated in service classes (AuthenticateServiceImpl, UserProfileServiceImpl).  
Repository Pattern → Database operations abstracted via UserRepository.  
DTO Pattern → Separates persistent entities from API-facing models.  
Strategy Pattern (PasswordEncoder) → Pluggable hashing algorithms.  
Factory Pattern (UserDetails.withUsername) → Builds security user objects.

## Notable Code Snippets / Tricks Worth Mentioning for Interviews
| Snippet | Explanation |
|--------|-------------|
| `authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user, pass));` | Standard Spring Security login flow. |
| `jwtUtil.generateToken(userDetails);` | Generates secure JWT access token. |
| `userRepository.findAll(pageable).map(this::toDTO);` | JPA pagination combined with DTO mapping. |
| `passwordEncoder.encode(dto.getPassword());` | Ensures hashed password storage. |
| `userRepository.findById(id).orElseThrow(() -> new NoSuchElementException());` | Idiomatic optional handling. |
| `List.of(role.split(","))` | Compact role parsing for multi-role accounts. |

## Potential Interview Questions & Short Concise Answers
- How does Spring Security authenticate a user? → By validating credentials via AuthenticationManager and returning an Authentication object.
- What is JWT used for in this project? → To generate stateless access and refresh tokens.
- Why encode passwords before saving? → To ensure passwords are never stored in plain text.
- How does pagination work with Spring Data JPA? → Pageable defines page parameters and Page returns paged results.
- How are DTOs and Entities connected? → Mapped manually using toDTO and toEntity methods.
- What happens if a user ID isn't found during search? → A NoSuchElementException is thrown.
- What does @Transactional ensure? → Atomicity and consistency during DB operations.
- How does loadUserByUsername work? → It fetches user details and converts roles into authorities.
- Why use SimpleGrantedAuthority? → To represent roles in a Spring Security-compliant format.
- What does DataSourceUtils.getConnection do? → Retrieves a DB connection synchronized with Spring’s transaction system.
- Why return ApiResponseDTO as wrapper? → To maintain unified API response structure.
- How do you check if a record exists before update/delete? → Using userRepository.existsById(id).
- Why map roles like “ROLE_ADMIN”? → Spring requires role prefix conventions for authorization.
- What triggers UsernameNotFoundException? → When a provided username does not exist.
- Why use logger.debug in service classes? → For observability and debugging in development.
- Why calculate pages using Math.ceil? → To verify if requested page exceeds available data.

## One-Liner Summary
A complete Spring Boot authentication and user profile management service using JWT, pagination, secure password handling, and robust CRUD operations.
