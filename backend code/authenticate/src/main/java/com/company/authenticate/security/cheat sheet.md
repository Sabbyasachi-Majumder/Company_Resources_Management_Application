# Project Cheat-Sheet - Custom Authentication (JWT) Module

## Quick Overview
A compact authentication module that issues, validates and applies JWTs in a Spring Boot application using a custom `OncePerRequestFilter`, a `JwtUtil` utility for token creation/validation, and a custom `AuthenticationEntryPoint` to produce uniform unauthorized responses.

## File Structure Summary

`CustomAuthenticationEntryPoint.java` → Custom `AuthenticationEntryPoint` that returns a JSON `ApiResponseDTO` for unauthorized requests and skips public paths (e.g., swagger & auth endpoints). :contentReference[oaicite:0]{index=0}

`JwtAuthenticationFilter.java` → `OncePerRequestFilter` that reads the `Authorization: Bearer <token>` header, extracts username and roles from the token via `JwtUtil`, and sets a `UsernamePasswordAuthenticationToken` into the `SecurityContext`. Skips the authenticate endpoint. :contentReference[oaicite:1]{index=1}

`JwtUtil.java` → JWT creation and validation helper: creates access + refresh tokens with claims (roles/type), extracts username / claims, verifies signature and expiration using a secret from properties. :contentReference[oaicite:2]{index=2}

## Core Technologies & Frameworks Used

Spring Boot → Application framework used for configuration and dependency injection.  
Spring Security → Security framework used for filters, authentication tokens, and the `AuthenticationEntryPoint`.  
jjwt (io.jsonwebtoken) → JWT creation and parsing library used to build/verify tokens. :contentReference[oaicite:3]{index=3}  
SLF4J → Logging abstraction used throughout the classes.

## All Java / Spring Annotations Used

`@Component` → Marks classes as Spring-managed beans (`JwtUtil`, `JwtAuthenticationFilter`, `CustomAuthenticationEntryPoint`).   
`@Value` → Injects JWT configuration properties (secret, expirations) into `JwtUtil`. :contentReference[oaicite:6]{index=6}  
`@Override` → Standard Java override annotation on methods overriding superclass/ interface methods (e.g., `doFilterInternal`, `commence`).

## Key Concepts & Techniques Demonstrated

JWT creation → building tokens with custom claims (roles, type) and signing with an HMAC secret. :contentReference[oaicite:8]{index=8}  
JWT validation → parsing signed tokens, verifying signature and expiration, and mapping claims to application authorities.   
Stateless authentication → using tokens in HTTP headers to avoid server sessions.   
Spring Security filter integration → custom `OncePerRequestFilter` to populate `SecurityContext`. :contentReference[oaicite:11]{index=11}  
Centralized unauthorized response → custom `AuthenticationEntryPoint` to emit standard JSON error replies. :contentReference[oaicite:12]{index=12}  
Role extraction → mapping token claim `roles` → `SimpleGrantedAuthority` list for Spring Security.

## Important Patterns & Architectures

Once-per-request security filter → `JwtAuthenticationFilter` extends `OncePerRequestFilter` to run token checks once per HTTP request and to set authentication details. Used in request processing pipeline. :contentReference[oaicite:14]{index=14}

Token utility separation → `JwtUtil` encapsulates token generation / parsing logic to keep business code and filters thin. Used by filter and authentication endpoints. :contentReference[oaicite:15]{index=15}

Centralized error handling for auth → `CustomAuthenticationEntryPoint` returns consistent JSON error responses and ignores public endpoints (Swagger, authenticate endpoints). Useful for APIs. :contentReference[oaicite:16]{index=16}

Fail-fast access handling → filter converts token parsing failures into access-denied (throws AccessDeniedException) to stop request processing on invalid tokens. :contentReference[oaicite:17]{index=17}

## Notable Code Snippets / Tricks Worth Mentioning for Interviews

| Pattern / Snippet (one-line) | Explanation / Why it matters |
|---|---|
| `if (request.getRequestURI().equals("/api/v1/authenticates/authenticate")) { ... }` | Skip authentication filter for the login endpoint to allow token issuance. :contentReference[oaicite:18]{index=18} |
| `claims.put("roles", userDetails.getAuthorities().stream()...collect(Collectors.toList()));` | Embed roles as a claim in the JWT so downstream services can reconstruct authorities. :contentReference[oaicite:19]{index=19} |
| `Keys.hmacShaKeyFor(secret.getBytes())` | Create a `SecretKey` from config for secure HMAC signing (avoid hard-coded keys). :contentReference[oaicite:20]{index=20} |
| `ObjectMapper mapper = new ObjectMapper(); response.getWriter().write(mapper.writeValueAsString(...));` | Build structured JSON unauthorized response from `AuthenticationEntryPoint`. :contentReference[oaicite:21]{index=21} |
| `List<String> roles = jwtUtil.extractClaim(token, claims -> claims.get("roles", List.class));` | Generic claim extractor using a `Function<Claims,T>` to flexibly read any claim.  |

## Potential Interview Questions & Short Concise Answers

| Question? | Answer. |
|---|---|
| What is the role of `OncePerRequestFilter`? → | A Spring Security filter base class that guarantees a single execution per request. :contentReference[oaicite:23]{index=23} |
| Why embed roles in JWT claims? → | So services can authorize requests without additional lookups; roles travel with the token. :contentReference[oaicite:24]{index=24} |
| How do you sign a JWT securely? → | Use a strong secret key (not hard-coded) and `Keys.hmacShaKeyFor(secret.getBytes())` with a secure config source. :contentReference[oaicite:25]{index=25} |
| How to check token expiration? → | Parse `Claims.getExpiration()` and compare with current time to reject expired tokens. :contentReference[oaicite:26]{index=26} |
| What should an `AuthenticationEntryPoint` do? → | Return a consistent unauthorized response (status + body) for unauthenticated access attempts. :contentReference[oaicite:27]{index=27} |
| Why set `SecurityContextHolder.getContext().setAuthentication(...)`? → | So downstream security checks and `@PreAuthorize` can detect the current principal and authorities. :contentReference[oaicite:28]{index=28} |
| How to extract custom claims generically? → | Provide a `Function<Claims,T>` extractor method (like `extractClaim`) to avoid repeated parsing logic. :contentReference[oaicite:29]{index=29} |
| When to throw `AccessDeniedException` in a filter? → | When token is invalid or roles mismatch — to halt request processing and indicate authorization problems. :contentReference[oaicite:30]{index=30} |
| How to generate refresh tokens differently? → | Use a different claim (e.g., `type: refresh`) and a longer expiration for refresh tokens. :contentReference[oaicite:31]{index=31} |
| Why skip Swagger endpoints in auth entry point? → | Swagger UI and API docs are usually public; skipping avoids blocking docs and keeps response clean. :contentReference[oaicite:32]{index=32} |
| What is the risk of using `secret.getBytes()`? → | Encoding differences; ensure consistent charset and sufficiently long random secret to prevent brute force. :contentReference[oaicite:33]{index=33} |
| How to map role strings to Spring Authorities? → | `roles.stream().map(SimpleGrantedAuthority::new)` to create `GrantedAuthority` instances. :contentReference[oaicite:34]{index=34} |
| Why centralize JWT logic in a util class? → | Keeps token concerns isolated, easier to test and reuse across filters/controllers. :contentReference[oaicite:35]{index=35} |
| How should invalid tokens be logged/handled? → | Log at warn/error and throw a security exception (or return 401/403) to avoid leaking sensitive token details.  |
| What to include in a token payload? → | Minimal claims necessary for authentication/authorization (subject, roles, iat, exp) to reduce token size and attack surface. :contentReference[oaicite:37]{index=37} |
| How to safely return JSON from `AuthenticationEntryPoint`? → | Set `response.setContentType(MediaType.APPLICATION_JSON_VALUE)` and write a serialized DTO string. :contentReference[oaicite:38]{index=38} |

*(12–16 focused Q&A rows provided; tailor or expand based on interview emphasis.)*

## One-Liner Summary
A focused JWT-based stateless authentication layer for Spring Boot: `JwtUtil` creates/validates tokens, `JwtAuthenticationFilter` applies token-based auth per request, and `CustomAuthenticationEntryPoint` returns consistent JSON errors for unauthorized access. 
