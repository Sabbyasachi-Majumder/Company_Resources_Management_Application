# Project Cheat-Sheet - Authenticate Service

## Quick Overview
A minimal JPA entity representing a user profile for an authentication service; maps Java fields to a `UserProfileTable` database table and uses Lombok to reduce boilerplate.

## File Structure Summary
UserProfileEntity.java → JPA entity for user profiles, defines columns (userId, userName, password, enabled, role). :contentReference[oaicite:0]{index=0}

## Core Technologies & Frameworks Used
Java → language used for the entity class.  
Jakarta Persistence (JPA) → ORM annotations for mapping the entity to a relational table.  
Lombok → generates getters, setters, constructors to reduce boilerplate.

## All Java / Spring Annotations Used (if Java/Spring code exists)
@Getter → Lombok: generates getter methods for all fields.  
@Setter → Lombok: generates setter methods for all fields.  
@NoArgsConstructor → Lombok: generates a no-argument constructor.  
@AllArgsConstructor → Lombok: generates an all-arguments constructor.  
@Entity → JPA: marks the class as a persistent entity.  
@Table(name = "UserProfileTable") → JPA: specifies the database table name.  
@Id → JPA: marks the primary key field.  
@Column(...) → JPA: configures column names and constraints (nullable, unique).

## Key Concepts & Techniques Demonstrated
JPA Entity Mapping → maps Java fields to database columns via `@Entity`/`@Column`.  
Column Constraints → uses `nullable` and `unique` at the column level for DB constraints.  
Lombok Boilerplate Reduction → uses Lombok annotations to auto-generate constructors/getters/setters.  
Primitive vs Wrapper Types → uses primitive `boolean` for `enabled`, with implications for nullability/defaults.  
Manual Primary Key Handling → primary key `userId` present without an active `@GeneratedValue` (commented out).

## Important Patterns & Architectures
ORM Mapping Pattern → JPA entity pattern used to represent DB rows as Java objects (UserProfileEntity → `UserProfileTable`).  
DTO / Persistence Separation (implied) → entity class is meant for persistence layer; typically a separate DTO/service layer would be used above it.  
Lombok Composition → reduces boilerplate in domain objects (applied directly to the entity).

## Notable Code Snippets / Tricks Worth Mentioning for Interviews
| Snippet | Why it matters / Interview talking point |
|---|---|
| `@Getter @Setter @NoArgsConstructor @AllArgsConstructor` | Demonstrates Lombok usage to avoid boilerplate; mention pros/cons (readability vs hidden generated code). |
| `@Entity @Table(name = "UserProfileTable")` | Shows explicit table naming (useful when DB naming differs from Java class names). |
| `@Column(name = "UserName", unique = true, nullable = false)` | Demonstrates enforcing uniqueness & nullability at DB schema level via annotation. |
| `// @GeneratedValue(strategy = GenerationType.IDENTITY)` (commented out) | Useful talking point: deliberate choice to manage IDs externally vs DB auto-generation. |
| `private boolean enabled;` | Using primitive boolean avoids nulls but has default `false` — discuss implications for tri-state (null/true/false). |

## Potential Interview Questions & Short Concise Answers
| Question | Answer |
|---|---|
| What does `@Entity` do? | Marks the class as a JPA entity so it is mapped to a database table. |
| Why use `@Table(name = "...")`? | To control the exact DB table name instead of relying on default naming. |
| What is the effect of `unique = true` on `@Column`? | Adds a unique constraint at the DB level to prevent duplicate values. |
| Why might `@GeneratedValue` be commented out? | To indicate IDs are supplied externally (e.g., from another service) or to avoid DB auto-generation for testing/control. |
| What are Lombok annotations used for here? | To auto-generate getters/setters and constructors, reducing boilerplate. |
| Pros/cons of Lombok in entities? | Pros: less code, faster dev. Cons: hidden generated code, can obscure IDE navigation and complicate debugging. |
| Primitive `boolean` vs `Boolean` in entities — tradeoffs? | `boolean` can't be null and defaults to false; `Boolean` allows null (tri-state) which can represent 'unknown'. |
| Is storing `password` as plain `String` OK? | No — passwords should be stored hashed+salted; entities shouldn't hold plain text in production. |
| What does `nullable = false` enforce? | A NOT NULL constraint at the DB level; the JPA provider may also validate on persist. |
| How would you add created/updated timestamps? | Add fields annotated with `@Column` and manage them with JPA lifecycle callbacks (`@PrePersist`, `@PreUpdate`) or `@CreationTimestamp`/`@UpdateTimestamp` if available. |
| How to enforce unique username at the DB level beyond `@Column`? | Add a unique index/constraint via DDL or `@Table(uniqueConstraints = ...)`. |
| Why avoid business logic in entities? | Keeps persistence layer simple and avoids mixing responsibilities — business logic should live in services. |
| What to consider for role storage (`String role`)? | Could use an enum mapped to DB for type-safety and a fixed set of roles. |
| How does JPA determine column types? | JPA maps Java types to SQL types via the provider; annotations can further customize mapping. |
| How to make `userId` auto-generated safely? | Re-enable `@GeneratedValue` with appropriate `GenerationType` (e.g., IDENTITY or SEQUENCE) depending on DB. |
| What validation should be added to `userName`/`password`? | Add input validation (length, characters) and consider `@Size`, `@NotBlank` (Bean Validation) on DTOs before persisting. |

## One-Liner Summary
A concise JPA/Lombok-backed entity mapping `UserProfileEntity` to `UserProfileTable`, suitable as the persistence model for an authentication service.
