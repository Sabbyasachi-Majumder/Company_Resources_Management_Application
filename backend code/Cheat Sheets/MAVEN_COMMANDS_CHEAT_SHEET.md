## Maven Commands Cheat Sheet

### Project: Company Resources Management Application (Multi-module Spring Boot Microservices)

### Run from the root/parent directory (where the main pom.xml with <modules> is located)

### Core Build & Test Commands

- mvn clean install ->Clean + compile + run ALL tests + package + install to local repo (standard for full build)
- mvn clean test ->Clean + compile + run tests only (fast feedback loop)
- mvn clean verify ->Clean + compile + run unit + integration tests (recommended for CI/CD)
- mvn clean package ->Clean + compile + test + package JARs (no install)

### Skip Tests (for quick iterations)

- mvn clean install -DskipTests ->Skip test execution
- mvn clean install -Dmaven.test.skip=true ->Skip test compilation & execution (faster)

### Run Specific Tests

- mvn clean test -Dtest=EmployeeServiceTest ->Run single test class
- mvn clean test -Dtest=EmployeeServiceTest#testCreateEmployee ->Run specific test method

### Parallel & Faster Builds

- mvn clean install -T 1C ->Use 1 thread per CPU core (significantly faster on multi-core machines)

### Spring Boot Specific

- mvn spring-boot:run ->Run a single service module directly (cd into the module first)
- mvn spring-boot:repackage ->Create executable fat JAR

### Useful for Debugging Builds

- mvn -X clean install ->Enable debug logging to see detailed Maven output
- mvn dependency:tree ->View full dependency tree (great for resolving conflicts)
- mvn dependency:analyze ->Detect unused/undeclared dependencies

### Clean Local Repository (rarely needed)

### If you suspect corrupted artifacts:

### Delete ~/.m2/repository/your-group-id/ and rebuild