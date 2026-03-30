# Arquitetura Hexagonal + Clean Architecture

Ports & Adapters com enforcement automatizado via ArchUnit.
Domínio com zero dependências externas. 59 testes, 95% cobertura.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green)
![Coverage](https://img.shields.io/badge/coverage-95%25-brightgreen)
![Tests](https://img.shields.io/badge/tests-59%20passing-brightgreen)
![CI](https://github.com/wesleytaumaturgo/spring-boot-hexagonal-clean-architecture/actions/workflows/ci.yml/badge.svg)

## Demo

```
✅ ArchUnit: domain NÃO importa infrastructure   → PASSOU
✅ ArchUnit: domain NÃO importa application      → PASSOU
✅ ArchUnit: domain NÃO usa annotations Spring   → PASSOU
✅ ArchUnit: application NÃO importa infra       → PASSOU
✅ 27 testes — Value Objects + entidade Product  → PASSOU
✅ 11 testes — ProductService (application)      → PASSOU
✅  9 testes — ProductController (MockMvc)       → PASSOU
✅  7 testes — Persistência (H2 + @DataJpaTest)  → PASSOU
✅  1 smoke test — @SpringBootTest               → PASSOU
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
   59 testes · 0 falhas · 95% cobertura · 4 regras ArchUnit · 0 violações
```

## Sobre

Hexagonal e Clean Architecture são discutidos em toda conferência,
mas poucos repos demonstram enforcement automatizado das boundaries.
Este projeto garante via ArchUnit que o domínio permanece isolado —
se alguém importar Spring no domain, o build quebra.

A camada de domínio é Java puro: sem Spring, sem JPA, sem annotations
de framework. Cada regra arquitetural tem um teste que falha se for
violada. Não é documentação — é código executável.

## Funcionalidades

- Domínio 100% Java puro (zero dependências de framework)
- 4 regras ArchUnit verificadas em todo `mvn test`
- Ports & Adapters com inversão de dependência real
- API REST documentada com SpringDoc / Swagger UI
- TDD estrito: commits separados RED → GREEN → REFACTOR

## Arquitetura

```
┌────────────────────────────────────────────────────────┐
│                     INFRAESTRUTURA                      │
│  ┌──────────────────┐       ┌────────────────────────┐ │
│  │  ProductController│       │ ProductPersistenceAdapter│ │
│  │  (REST Adapter)  │       │    (JPA Adapter)       │ │
│  └────────┬─────────┘       └───────────┬────────────┘ │
│           │ ProductUseCase              │ ProductRepository
│  ┌────────▼──────────────────────────────▼───────────┐ │
│  │                APLICAÇÃO                           │ │
│  │                ProductService                      │ │
│  └──────────────────────┬────────────────────────────┘ │
│                         │                               │
│  ┌──────────────────────▼────────────────────────────┐ │
│  │                  DOMÍNIO                           │ │
│  │  Product · ProductName · Money · CategoryId        │ │
│  │      *** ZERO dependências de framework ***        │ │
│  └───────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────┘
```

Diagramas C4 completos: [`docs/architecture/ARCHITECTURE.md`](docs/architecture/ARCHITECTURE.md)

## Stack

| Camada | Tecnologia |
|--------|-----------|
| Core | Java 21 · Spring Boot 3.3.4 |
| Persistência | Spring Data JPA · H2 (testes) · PostgreSQL 16 |
| Testes | JUnit 5 · Mockito · ArchUnit 1.3 · @DataJpaTest |
| Docs | SpringDoc OpenAPI 2.5 · Swagger UI |
| Infraestrutura | Docker · Docker Compose · GitHub Actions |

## Como Rodar

```bash
git clone https://github.com/wesleytaumaturgo/spring-boot-hexagonal-clean-architecture.git
cd spring-boot-hexagonal-clean-architecture
docker compose up -d
mvn test
# Swagger UI: http://localhost:8080/swagger-ui.html
# Health:     http://localhost:8080/actuator/health
```

## API

| Método | Path | Respostas |
|--------|------|-----------|
| `POST` | `/api/v1/products` | 201 / 409 / 422 |
| `GET` | `/api/v1/products/{id}` | 200 / 404 |
| `GET` | `/api/v1/products` | 200 (paginado) |
| `PUT` | `/api/v1/products/{id}` | 200 / 404 / 422 |
| `PATCH` | `/api/v1/products/{id}/deactivate` | 200 / 404 / 409 |

## ADRs

| ID | Decisão |
|----|---------|
| [ADR-001](docs/adr/records/ADR-001-hexagonal-architecture.md) | Hexagonal Architecture |
| [ADR-002](docs/adr/records/ADR-002-spring-data-jpa-persistence-adapter.md) | Spring Data JPA adapter |
| [ADR-003](docs/adr/records/ADR-003-h2-for-test-database.md) | H2 in-memory para testes |

## English

REST API demonstrating Hexagonal Architecture (Ports & Adapters) with automated boundary enforcement via ArchUnit.
Domain layer has zero framework dependencies — if anyone adds a Spring annotation to the domain, the build fails.
59 tests · 95% line coverage · Java 21 + Spring Boot 3.3 · `docker compose up -d && mvn test`.

## Licença

MIT
