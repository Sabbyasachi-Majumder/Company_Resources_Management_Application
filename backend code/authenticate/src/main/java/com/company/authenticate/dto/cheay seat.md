# Project Cheat-Sheet - Authenticate Service

## Quick Overview
A set of Java DTOs for an authentication/user-profile service: request/response objects, validation rules, and a generic API response wrapper used to pass data and metadata (status/message) between layers and over REST.

## File Structure Summary

`AuthRequestDTO.java` → DTO for authentication requests (username + password) with validation.  
`AuthResponseDTO.java` → DTO for authentication responses containing JWT access and refresh tokens.  
`RefreshRequestDTO.java` → DTO for refresh-token requests (contains refreshToken).  
`ApiResponseDTO.java` → Generic API response wrapper `ApiResponseDTO<T>` holding `status`, `message`, and `data`.  
`UserProfileDTO.java` → DTO representing a single user's profile fields and validation (userId, userName, password, role, enabled).  
`UserProfileRequestDTO.java` → Request DTO wrapping a `userProfileList` (validated, non-empty) for bulk create/update.  
`UserProfileResponseDTO.java` → Response DTO wrapping `userProfileList` and a list of `ApiResponseDTO<UserProfileResponseDTO>` for per-item operation results.

## Core Technologies & Frameworks Used

Java → language used for DTO classes.  
Lombok → generates getters/setters and constructors (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`).  
Jakarta Validation (Bean Validation) → field-level validation (`@NotBlank`, `@Size`, `@Pattern`, `@NotEmpty`, `@Valid`).  
Jackson JSON → JSON serialization control (`@JsonInclude`, `@JsonProperty`, `@JsonIgnoreProperties`).  
OpenAPI / Swagger annotations → schema documentation (`@Schema`).  
Collections (java.util.ArrayList) → lists for bulk DTO payloads.

## All Java / Spring Annotations Used (if Java/Spring code exists)

`@Getter` → Lombok: generate getter methods.  
`@Setter` → Lombok: generate setter methods.  
`@NoArgsConstructor` → Lombok: generate a no-argument constructor.  
`@AllArgsConstructor` → Lombok: generate an all-arguments constructor.  
`@Schema` → OpenAPI: document fields/classes for API docs.  
`@NotBlank` → Jakarta Validation: require non-null, non-empty (trimmed) string.  
`@Size` → Jakarta Validation: enforce min/max string length.  
`@Pattern` → Jakarta Validation: enforce regex for field values.  
`@NotEmpty` → Jakarta Validation: require a collection/array not be empty.  
`@Valid` → Jakarta Validation: cascade validation to nested objects/collections.  
`@JsonInclude` → Jackson: control what values are included during serialization (e.g., `NON_NULL`).  
`@JsonProperty` → Jackson: bind JSON property name to field.  
`@JsonIgnoreProperties` → Jackson: ignore unknown JSON properties on deserialization.

## Key Concepts & Techniques Demonstrated

DTO Pattern → clear separation of transport objects from domain models.  
Field-level Validation → declarative input validation via Jakarta annotations.  
Generic Response Wrapper → reusable `ApiResponseDTO<T>` to standardize API responses.  
JSON Serialization Control → reduce payload noise with `@JsonInclude(JsonInclude.Include.NON_NULL)`.  
Bulk Operations DTOs → list-wrapping request/response objects (supports multi-entity operations).  
Lombok Boilerplate Reduction → concise classes without manual getters/setters/constructors.

## Important Patterns & Architectures

DTO Pattern → used across all files to decouple API layer from domain/business logic (all DTO classes).  
Generic Response Pattern → `ApiResponseDTO<T>` provides consistent API contract for success/error + payload (used in `UserProfileResponseDTO`).  
Validation Cascade Pattern → `@Valid` + `@NotEmpty` on `UserProfileRequestDTO.userProfileList` to validate nested `UserProfileDTO` objects.  
Selective Serialization Pattern → `@JsonInclude(JsonInclude.Include.NON_NULL)` used to avoid null fields in JSON responses (applied to multiple DTOs).  
OpenAPI Documentation Pattern → `@Schema` annotations to generate API docs and examples (applied to fields/classes).

## Notable Code Snippets / Tricks Worth Mentioning for Interviews

| Pattern | Example & Comment |
|---|---|
| Generic API wrapper | `public class ApiResponseDTO<T> { private String status; private String message; private T data; }` — standardizes status/message/data across endpoints. |
| Validation on list payloads | `@Valid @NotEmpty ArrayList<UserProfileDTO> userProfileList;` — enforces non-empty collection and validates each element. |
| JSON null filtering | `@JsonInclude(JsonInclude.Include.NON_NULL)` — keeps responses compact by omitting `null` fields. |
| Role validation via regex | `@Pattern(regexp = "^(admin|user)$") private String role;` — constrains role to allowed values at DTO level. |
| Lombok constructors/getters | `@Getter @Setter @NoArgsConstructor @AllArgsConstructor` — cut boilerplate and keep DTO concise. |

## Potential Interview Questions & Short Concise Answers

| Question | Answer |
|---|---|
| What is a DTO and why use it? | DTO (Data Transfer Object) is a simple object to transfer data between layers or services; it separates API contract from domain models. |
| Why use a generic `ApiResponseDTO<T>`? | To unify API responses with a predictable `status`, `message`, and `data` shape across endpoints. |
| What does `@JsonInclude(JsonInclude.Include.NON_NULL)` do? | It omits fields with `null` values from serialized JSON, reducing payload size. |
| How does `@Valid` work on a collection field? | `@Valid` triggers validation of each element in the collection when used with a validating framework. |
| Difference between `@NotBlank` and `@NotEmpty`? | `@NotBlank` applies to strings and disallows null/empty/whitespace; `@NotEmpty` applies to collections/strings to disallow null or empty. |
| Why use `@Pattern` for role field? | To enforce allowed role values (`admin` or `user`) at the DTO level via regex. |
| What is Lombok and why use it here? | Lombok auto-generates boilerplate (getters/setters/constructors), making DTOs concise and readable. |
| How to represent bulk operations in DTOs? | Wrap multiple entities in a collection field (e.g., `ArrayList<UserProfileDTO> userProfileList`) with validation. |
| When to use `@JsonProperty`? | To control or rename the JSON property name independent of the Java field name. |
| Why include examples in `@Schema`? | Example values in `@Schema` improve generated OpenAPI docs and make API usage clearer to consumers. |
| How to handle optional fields in responses? | Use `@JsonInclude(NON_NULL)` and nullable fields so omitted values don't appear in JSON. |
| What are pros/cons of putting validation in DTOs? | Pros: early input validation and clearer contracts. Cons: duplication if domain model has different rules; validation logic may be scattered. |
| How does `ApiResponseDTO<T>` support error reporting? | Set `status = "error"` and provide `message` explaining the failure; `data` can optionally carry details. |
| Why use `ArrayList` instead of `List` in DTOs? | `ArrayList` is concrete and easily serializable; but using the `List` interface is generally preferred for flexibility. |
| What does `@JsonIgnoreProperties(ignoreUnknown = true)` do? | It prevents deserialization errors when unexpected JSON properties are present, ignoring them instead. |
| How to validate password length? | Use `@Size(min=8, max=16)` on the password field to enforce length constraints. |

## One-Liner Summary
A compact collection of validated, Jackson-ready Java DTOs (with Lombok and OpenAPI annotations) that standardize auth and user-profile request/response payloads and provide a reusable generic API response format.
