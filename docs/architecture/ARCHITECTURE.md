# Architecture — Hexagonal (Ports & Adapters)

## Overview

This project implements the Hexagonal Architecture pattern (also known as Ports & Adapters), where the domain model is completely isolated from external concerns (frameworks, databases, HTTP).

```
┌────────────────────────────────────────────────────────┐
│                     Infrastructure                      │
│  ┌──────────────┐         ┌──────────────────────────┐ │
│  │   REST API   │         │      PostgreSQL / H2      │ │
│  │  (Adapter)   │         │       (Adapter)           │ │
│  └──────┬───────┘         └────────────┬─────────────┘ │
│         │ Port IN                      │ Port OUT       │
│  ┌──────▼──────────────────────────────▼─────────────┐ │
│  │              Application Layer                     │ │
│  │              (ProductService)                      │ │
│  └──────────────────────┬────────────────────────────┘ │
│                         │                               │
│  ┌──────────────────────▼────────────────────────────┐ │
│  │                 Domain Layer                       │ │
│  │  Product · ProductName · Money · CategoryId        │ │
│  │  ProductRepository (port) · ProductUseCase (port)  │ │
│  └───────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────┘
```

---

## C4 Level 1 — System Context

```mermaid
C4Context
    title Product API — System Context

    Person(client, "API Client", "Frontend, mobile app or CLI tool")
    System(api, "Product API", "Spring Boot REST API managing products")
    SystemDb(db, "PostgreSQL", "Stores product data")

    Rel(client, api, "HTTP/JSON", "REST")
    Rel(api, db, "JDBC/JPA", "TCP 5432")
```

---

## C4 Level 2 — Container

```mermaid
C4Container
    title Product API — Containers

    Person(client, "API Client")

    Container(rest, "REST Controller", "Spring MVC @RestController", "Handles HTTP requests and responses")
    Container(service, "ProductService", "Java 21 POJO", "Orchestrates domain use cases")
    Container(domain, "Domain Model", "Java 21 records/classes", "Product, value objects, port interfaces")
    Container(jpa, "Persistence Adapter", "Spring Data JPA", "Implements ProductRepository port")
    ContainerDb(db, "PostgreSQL", "PostgreSQL 16", "products table")

    Rel(client, rest, "HTTP POST/GET/PUT/PATCH", "JSON")
    Rel(rest, service, "calls", "ProductUseCase port")
    Rel(service, domain, "uses", "domain objects")
    Rel(service, jpa, "calls via", "ProductRepository port")
    Rel(jpa, db, "reads/writes", "JDBC")
```

---

## C4 Level 3 — Component (Infrastructure Layer)

```mermaid
C4Component
    title Infrastructure Layer — Components

    Component(ctrl, "ProductController", "adapter/in/rest", "REST endpoints: create, find, list, update, deactivate")
    Component(handler, "GlobalExceptionHandler", "adapter/in/rest", "Maps domain exceptions to HTTP status codes")
    Component(dto_in, "CreateProductRequest / UpdateProductRequest", "adapter/in/rest/dto", "Validated request DTOs (@Valid, @NotBlank, @Positive)")
    Component(dto_out, "ProductResponse", "adapter/in/rest/dto", "Response DTO mapping from domain Product")
    Component(adapter, "ProductPersistenceAdapter", "adapter/out/persistence", "Implements ProductRepository; maps domain ↔ JPA entity")
    Component(repo, "SpringDataProductRepository", "adapter/out/persistence", "JPA repository with filtered JPQL query")
    Component(entity, "ProductJpaEntity", "adapter/out/persistence", "JPA entity; package-private (encapsulated)")
    Component(config, "BeanConfig", "config", "Wires ProductService as ProductUseCase Spring bean")

    Rel(ctrl, dto_in, "deserializes")
    Rel(ctrl, dto_out, "serializes")
    Rel(ctrl, handler, "exception propagation")
    Rel(adapter, repo, "delegates to")
    Rel(adapter, entity, "maps to/from")
    Rel(config, adapter, "injects into ProductService")
```

---

## Package Structure

```
src/main/java/com/example/hexagonal/
├── HexagonalApplication.java               ← Spring Boot entry point
│
├── domain/                                 ← PURE JAVA — no framework deps
│   ├── model/
│   │   ├── Product.java
│   │   └── ProductStatus.java
│   ├── valueobject/
│   │   ├── CategoryId.java
│   │   ├── Money.java
│   │   └── ProductName.java
│   ├── port/
│   │   ├── in/  ProductUseCase.java        ← input port (interface)
│   │   └── out/ ProductRepository.java     ← output port (interface)
│   └── exception/
│       ├── ProductNotFoundException.java
│       ├── ProductAlreadyExistsException.java
│       └── ProductAlreadyInactiveException.java
│
├── application/                            ← USE CASES — depends only on domain
│   └── ProductService.java
│
└── infrastructure/                         ← ADAPTERS — depends on frameworks
    ├── adapter/
    │   ├── in/
    │   │   └── rest/
    │   │       ├── ProductController.java
    │   │       ├── GlobalExceptionHandler.java
    │   │       └── dto/
    │   │           ├── CreateProductRequest.java
    │   │           ├── UpdateProductRequest.java
    │   │           └── ProductResponse.java
    │   └── out/
    │       └── persistence/
    │           ├── ProductJpaEntity.java           (package-private)
    │           ├── SpringDataProductRepository.java (package-private)
    │           └── ProductPersistenceAdapter.java
    └── config/
        └── BeanConfig.java
```

---

## Dependency Rules (enforced by ArchUnit)

| Rule | Direction | Status |
|------|-----------|--------|
| domain → infrastructure | FORBIDDEN | ✅ Enforced |
| domain → application | FORBIDDEN | ✅ Enforced |
| domain → Spring stereotypes | FORBIDDEN | ✅ Enforced |
| application → infrastructure | FORBIDDEN | ✅ Enforced |

---

## API Endpoints

| Method | Path | Description | Status |
|--------|------|-------------|--------|
| `POST` | `/api/v1/products` | Create product | 201 / 409 / 422 |
| `GET` | `/api/v1/products/{id}` | Find by ID | 200 / 404 |
| `GET` | `/api/v1/products` | List (pageable, filterable) | 200 |
| `PUT` | `/api/v1/products/{id}` | Update | 200 / 404 / 422 |
| `PATCH` | `/api/v1/products/{id}/deactivate` | Deactivate | 200 / 404 / 409 |

---

## ADRs

| ID | Title | Status |
|----|-------|--------|
| [ADR-001](../adr/records/ADR-001-hexagonal-architecture.md) | Hexagonal Architecture | ACCEPTED |
| [ADR-002](../adr/records/ADR-002-spring-data-jpa-persistence-adapter.md) | Spring Data JPA as Persistence Adapter | ACCEPTED |
| [ADR-003](../adr/records/ADR-003-h2-for-test-database.md) | H2 In-Memory Database for Tests | ACCEPTED |
