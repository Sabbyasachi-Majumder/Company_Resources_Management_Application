# Project Cheat-Sheet - Configuration Module

## Quick Overview
This module contains application-level configuration for Spring Boot, including environment properties and logging setup.

## File Structure Summary
application.yml → Defines Spring Boot application properties, server config, database settings, and environment variables.  
logback-spring.xml → Defines logging levels, appenders, log formatting, and log file rotation.

## Core Technologies & Frameworks Used
YAML → Human-readable hierarchical configuration format.  
Spring Boot Configuration → Centralized application property management.  
Logback → High-performance logging framework used by Spring Boot.  
SLF4J → Logging abstraction used to bind Logback.

## All Java / Spring Annotations Used (if Java/Spring code exists)
(No Java classes found in provided files.)

## Key Concepts & Techniques Demonstrated
Externalized Configuration → Separates config from code for environment flexibility.  
Profile-Based Properties → Allows different settings per environment (dev, prod, etc.).  
Centralized Logging → Unified log pattern formatting and file rotation.  
Appender-Based Architecture → Log output routing to console and file.  
Property Injection → Drives server, datasource, and app behavior without changing code.

## Important Patterns & Architectures
Environment-Based Configuration → application.yml uses hierarchical keys for layered config.  
Appender-Pattern Logging → logback-spring.xml routes logs to console/file using modular appenders.  
Rolling File Logging → File rotation prevents log bloat in production systems.  
Configuration-First Architecture → Behavior changes through config rather than code edits.

## Notable Code Snippets / Tricks Worth Mentioning for Interviews
| Snippet | Explanation |
|--------|-------------|
| `spring.profiles.active: dev` | Activates specific runtime profile. |
| `${VAR_NAME:default}` | Environment variable with fallback value. |
| `<springProperty scope="context" ... />` | Injects Spring properties into Logback config. |
| `<rollingPolicy>` | Enables log rotation based on size or date. |
| `logging.level.<package>=DEBUG` | Fine-grained log level control. |

## Potential Interview Questions & Short Concise Answers
- What is the purpose of `application.yml` in Spring Boot? → It centralizes configuration in a structured format.
- Why use YAML instead of properties files? → YAML supports cleaner hierarchy and reduces repetition.
- How do Spring profiles work? → They activate environment-specific settings.
- What is Logback in Spring Boot? → The default logging framework used through SLF4J.
- Why define appenders in logback-spring.xml? → To control where logs are written (console, file, etc.).
- What is log rolling? → Automatic archiving of old logs to manage file size.
- How do environment variables get injected into YAML? → Via `${ENV_VAR:default}` syntax.
- Why externalize DB credentials? → To avoid exposing secrets in code.
- How do you change log levels per package? → Using `logging.level.<package>=LEVEL`.
- What does `<encoder>` do in a Logback appender? → Defines log output formatting.
- Why is `logback-spring.xml` preferred over `logback.xml`? → It allows use of Spring extensions in logging.
- How can configuration errors be debugged? → Enable debug logs or use `--debug` at startup.
- What happens if an environment variable in `${...}` is missing? → The default value (if provided) is used.
- How does Spring load configuration files? → In an ordered manner: application.properties → application.yml → profile-specific files.
- Why use rolling policies in production? → To prevent disk exhaustion from growing logs.
- What is the benefit of separating console and file appenders? → Fine-grained control over log destinations.

## One-Liner Summary
Centralized configuration module defining application properties and full Logback logging strategy for Spring Boot.
