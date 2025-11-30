# Project Cheat-Sheet - AuthenticateApplication

## Quick Overview
A Spring Boot application entry point enabling service discovery for the authentication service.

## File Structure Summary
**AuthenticateApplication.java** → Main Spring Boot application class bootstrapping the authentication service.

## Core Technologies & Frameworks Used
Spring Boot → Provides auto-configuration and application bootstrap.
Spring Cloud Discovery → Enables service registration and discovery.
Spring MVC Auto-Configuration Exclusion → Disables default error MVC auto-config.

## All Java / Spring Annotations Used
@SpringBootApplication → Marks the main Spring Boot application and enables auto-configuration.
@EnableDiscoveryClient → Allows the service to register with a discovery server (e.g., Eureka).

## Key Concepts & Techniques Demonstrated
Spring Boot bootstrap class → Centralized entry point for the application.
Microservice registration → Allows integration with service discovery tools.
Configuration exclusion → Disables default behavior to customize error handling.

## Important Patterns & Architectures
Bootstrapping Pattern → Used in `main()` to start the Spring Boot app via `SpringApplication.run`.

## Notable Code Snippets / Tricks Worth Mentioning for Interviews
| Snippet | Explanation |
|--------|-------------|
| `SpringApplication.run(App.class, args);` | Standard way to bootstrap a Spring Boot application. |
| `@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})` | Demonstrates how to disable specific auto-configurations. |
| `@EnableDiscoveryClient` | Registers service with Eureka/Consul for microservice discovery. |
| `public static void main(String[] args)` | Java entry point method required to launch Spring Boot. |
| `package com.company.authenticate;` | Proper namespacing for modular Java apps. |

## Potential Interview Questions & Short Concise Answers
- What does `@SpringBootApplication` do? → Combines configuration, auto-configuration, and component scan.
- Why exclude `ErrorMvcAutoConfiguration`? → To override or customize default MVC error handling.
- What is the purpose of `EnableDiscoveryClient`? → Registers the service with a discovery server like Eureka.
- How does Spring Boot start an application? → Via `SpringApplication.run`.
- Why use a separate main application class? → Provides a clean bootstrapping entry point.
- What is service discovery in microservices? → Mechanism for services to locate each other dynamically.
- What happens if multiple auto-configurations conflict? → You can exclude specific ones with `exclude = {...}`.
- Why keep application classes in a base package? → Ensures component scanning works automatically.
- What is the significance of the `main` method? → Standard JVM entry point.
- How does Spring Boot perform auto-configuration? → By analyzing classpath and environment properties.
- Why is component scanning important? → Automatically detects and registers beans.
- What is Eureka in Spring Cloud? → A discovery server used to register and locate microservices.

## One-Liner Summary
A minimal Spring Boot microservice entry-point class enabling discovery-client registration for authentication services.
