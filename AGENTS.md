# AI Agent Guidelines for Mica

This repository contains the `mica` project, a Spring Cloud enhanced toolkit.

## 1. Build, Lint, and Test

The project uses **Maven** for build and dependency management.
Java Version: **17**

### Common Commands
*   **Full Build (Install):**
    ```bash
    mvn clean install -DskipTests
    ```
*   **Run All Tests:**
    ```bash
    mvn test
    ```
*   **Run Tests in a Specific Module:**
    ```bash
    mvn -pl mica-core test
    ```
*   **Run a Single Test Class:**
    ```bash
    # Run from root, specifying the module is recommended but optional if unique
    mvn -pl mica-core test -Dtest=StringUtilTest
    ```
*   **Run a Single Test Method:**
    ```bash
    mvn -pl mica-core test -Dtest=StringUtilTest#testFormat1
    ```

### Linting & Formatting
*   The project uses `.editorconfig` for formatting rules.
*   **Java**: Use **Tabs** for indentation.
*   **XML/JSON/YAML**: Use **Spaces** (2 or 4 as per file type, check `.editorconfig`).
*   **Encoding**: UTF-8.

## 2. Code Style & Conventions

### Java Style
*   **Indentation**: **MUST use Tabs** for `.java` files (as defined in `.editorconfig`).
*   **Naming**:
    *   Classes: `PascalCase`
    *   Methods/Variables: `camelCase`
    *   Constants: `UPPER_SNAKE_CASE`
*   **Imports**:
    *   Group imports: Standard Java -> javax/jakarta -> org/com -> net.dreamlu.
    *   Wildcard imports (e.g., `import org.springframework.util.*;`) are used in existing code, but specific imports are preferred for clarity unless importing many classes from a package.
*   **Annotations**:
    *   Use `@Nullable` (`org.jspecify.annotations.Nullable`) for nullable parameters/returns.
    *   Use Spring's `@Contract` where applicable.
*   **Lombok**: The project uses Lombok (e.g., `@Data`, `@RequiredArgsConstructor`, `@Getter`, `@Setter`). Use it to reduce boilerplate.
*   **Comments**:
    *   Javadoc is required for public classes and methods.
    *   Existing comments and Javadoc are in **Chinese**. Maintain this convention if modifying existing code or adding new utilities.

### Project Structure
*   **Modules**: The project is split into multiple modules (`mica-core`, `mica-http`, etc.).
*   **Dependencies**:
    *   Versions are managed in the root `pom.xml` or `mica-bom`. Avoid hardcoding versions in submodules; use managed properties.
    *   Utilize `mica-auto` for auto-configuration generation if creating starters.

### Testing
*   **Framework**: JUnit 5 (`org.junit.jupiter.api`).
*   **Assertions**: Use `org.junit.jupiter.api.Assertions`.
*   **Location**: Tests reside in `src/test/java`.
*   **Naming**: Test classes should end with `Test` (e.g., `StringUtilTest`).
*   **Scope**: Test classes and methods should generally be package-private (default in JUnit 5), public is not required.

### Error Handling
*   Use standard Java exceptions or project-specific exceptions (check `mica-core` for `ServiceException` etc.).
*   Avoid swallowing exceptions; log or rethrow appropriately.

## 3. General Guidelines
*   **Consistency**: Always check surrounding code for style and patterns before editing.
*   **Utility Classes**: Check `mica-core` for existing utilities (e.g., `StringUtil`, `ObjectUtil`) before writing new ones to avoid duplication.
*   **Configuration**: Respect `pom.xml` dependencies and versions.
