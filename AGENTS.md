# Repository Guidelines

## Project Structure & Module Organization
The repository root contains shared docs and local JDBC drivers in `lib/`. Main development happens under `hcframe-parent/`, a Maven aggregator for the platform modules: `hcframe-base` (shared framework code), `hcframe-user`, `hcframe-config`, `hcframe-gateway`, `hcframe-es`, `hcframe-activiti`, `hcframe-test`, `hcframe-spider`, and `hcframe-starter`. Java sources live in `src/main/java`, configuration and MyBatis XML live in `src/main/resources`, and tests belong in `src/test/java`.

## Build, Test, and Development Commands
Run commands from `hcframe-parent/` unless you are targeting one module.

```bash
mvn install
mvn -q -DskipTests compile
mvn -pl hcframe-user spring-boot:run -Dspring-boot.run.profiles=dev
mvn -pl hcframe-activiti -DskipTests=false test
```

`mvn install` builds and installs all modules. `compile` is the fastest sanity check for cross-module changes. `spring-boot:run` is appropriate for app modules that declare the plugin, such as `hcframe-user` or `hcframe-config`. Use `-pl <module>` to limit scope while iterating.

## Coding Style & Naming Conventions
This codebase targets Java 21 and uses Spring Boot, Lombok, MyBatis, and YAML configuration. Follow the existing Java style: 4-space indentation, `UpperCamelCase` class names, `lowerCamelCase` methods and fields, and package names under `com.taixingyiji.<area>`. Keep common suffixes consistent: `*Controller`, `*Service`, `*ServiceImpl`, `*Dao`, and `*Application`. Store SQL mappings in `src/main/resources/mapping/` and keep profile-specific config in `application-<profile>.yml`.

## Testing Guidelines
Testing uses `spring-boot-starter-test` with JUnit 5. Name test classes `*Test` or `*Tests` and place them in `src/test/java` beside the module they exercise. The parent POM sets Surefire to skip tests by default, so use `-DskipTests=false` whenever you need actual execution. Prefer focused module-level tests before running the full reactor.

## Commit & Pull Request Guidelines
Recent history uses short, imperative commit subjects with prefixes such as `fix:`. Continue that pattern, for example `fix: correct datasource routing in gateway`. Keep each commit scoped to one concern. Pull requests should list affected modules, note any required profile or environment changes, link the issue when available, and include screenshots only for UI or Swagger-visible changes.

## Security & Configuration Tips
Do not commit real environment secrets. Several POM profiles and `application*.yml` files contain host-specific settings; treat them as examples and prefer local overrides for credentials, Nacos endpoints, Redis, and CAS values.
