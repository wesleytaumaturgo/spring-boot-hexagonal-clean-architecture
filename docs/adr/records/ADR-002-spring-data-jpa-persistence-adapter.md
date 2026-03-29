# ADR-002: Spring Data JPA as Persistence Adapter

**Status:** ACCEPTED
**Date:** 2026-03-29

## Context

The domain layer defines a `ProductRepository` port (output port) with no persistence technology assumptions. We need to choose the technology to implement this port in the infrastructure layer.

## Decision

Use Spring Data JPA with an intermediate `ProductPersistenceAdapter` class that:
1. Implements the `ProductRepository` port (domain interface)
2. Delegates to a package-private `SpringDataProductRepository` (JPA repository)
3. Handles the mapping between `Product` domain objects and `ProductJpaEntity`

The `SpringDataProductRepository` is intentionally package-private — it is an implementation detail of the infrastructure layer and must not be accessible from the domain or application layers.

## Alternatives Considered

| Alternative | Reason rejected |
|---|---|
| Direct `@Repository` on domain port | Violates hexagonal architecture — domain would depend on Spring |
| MyBatis | More boilerplate for CRUD operations; less ecosystem integration with Spring Boot |
| JOOQ | Better for complex queries but overkill for this CRUD use case |
| Plain JDBC | No ORM mapping; higher maintenance burden |

## Consequences

**Positive:**
- Domain layer remains free of persistence annotations
- `ProductRepository` port can be swapped (e.g., for a NoSQL adapter) without touching domain or application code
- `@DataJpaTest` allows fast, isolated persistence tests with H2

**Negative:**
- Two-layer mapping (domain ↔ JPA entity) adds boilerplate
- JPA proxies can cause issues with value objects if not careful (mitigated by using `@Column` on primitive types, not embeddables)

## References
- ADR-001: Hexagonal Architecture decision
- `ProductPersistenceAdapter.java` — infrastructure implementation
- `ProductRepository.java` — domain port
