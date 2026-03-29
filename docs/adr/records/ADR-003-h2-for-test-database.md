# ADR-003: H2 In-Memory Database for Tests

**Status:** ACCEPTED
**Date:** 2026-03-29

## Context

Integration tests for the persistence layer need a real database to execute JPA queries. Using the production PostgreSQL database is not appropriate for unit/integration tests. We need an alternative.

## Decision

Use H2 in-memory database in `MODE=PostgreSQL` for all `@DataJpaTest` and `@SpringBootTest` test slices.

Configuration in `src/test/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:hexagonal;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
```

## Alternatives Considered

| Alternative | Reason rejected |
|---|---|
| Testcontainers (PostgreSQL) | Requires Docker in CI; slower startup; overkill for CRUD queries |
| Embedded PostgreSQL | Heavier dependency; longer build times |
| Mocking the repository | Defeats purpose of persistence integration tests |

## Consequences

**Positive:**
- Tests start in < 1 second (no container spin-up)
- No external dependencies in CI
- H2 PostgreSQL compatibility mode handles most standard SQL

**Negative:**
- H2 is not 100% PostgreSQL compatible (e.g., some PG-specific functions differ)
- If PostgreSQL-specific queries are added, Testcontainers will be needed

## When to Revisit

Switch to Testcontainers when the project uses PostgreSQL-specific features (JSON columns, arrays, full-text search, advisory locks).

## References
- `src/test/resources/application.yml`
- `ProductRepositoryIT.java`
