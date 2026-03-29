# API Hexagonal — Clean Architecture

> API REST com Arquitetura Hexagonal e Clean Architecture.
> A camada de domínio tem ZERO dependências de framework.
> Testes ArchUnit garantem as fronteiras em cada build.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green)
![Coverage](https://img.shields.io/badge/coverage-95%25-brightgreen)
![Tests](https://img.shields.io/badge/tests-59%20passing-brightgreen)
![CI](https://github.com/wesleytaumaturgo/spring-boot-hexagonal-clean-architecture/actions/workflows/ci.yml/badge.svg)
![Licença](https://img.shields.io/badge/Licen%C3%A7a-MIT-blue)

## Demo

```
✅ ArchUnit: domain NÃO importa infrastructure           → PASSOU
✅ ArchUnit: domain NÃO importa application              → PASSOU
✅ ArchUnit: domain NÃO usa annotations Spring           → PASSOU
✅ ArchUnit: application NÃO importa infrastructure      → PASSOU
✅ 27 testes de domínio (Value Objects + Product)        → PASSOU
✅ 11 testes de aplicação (ProductService)               → PASSOU
✅  9 testes de controller (MockMvc)                     → PASSOU
✅  7 testes de persistência (H2 @DataJpaTest)           → PASSOU
✅  1 smoke test (@SpringBootTest context loads)         → PASSOU
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
   59 testes · 0 falhas · 95% cobertura de linhas
```

## Sobre

A maioria dos projetos Spring Boot mistura regras de negócio com
controllers e repositories. Este projeto demonstra a separação
radical: o core do negócio é Java puro — sem Spring, sem JPA,
sem annotations de framework.

Testes ArchUnit rodam em toda build e impedem que qualquer
desenvolvedor quebre as fronteiras arquiteturais, mesmo sem querer.

## Funcionalidades

- Separação completa Domain / Application / Infrastructure
- Domain layer 100% Java puro (zero dependências de framework)
- ArchUnit validando fronteiras automaticamente no build
- Ports & Adapters com inversão de dependência real
- Testes unitários no domínio sem mocks de framework
- API documentada com SpringDoc OpenAPI / Swagger UI
- Quality gate (cobertura + segurança + docs) em `scripts/run-gate.sh`

## Arquitetura

```
┌────────────────────────────────────────────────────────┐
│                     INFRAESTRUTURA                      │
│  ┌──────────────────┐       ┌────────────────────────┐ │
│  │  ProductController│       │ ProductPersistenceAdapter│ │
│  │  (REST Adapter)  │       │   (JPA Adapter)        │ │
│  └────────┬─────────┘       └────────────┬───────────┘ │
│           │ ProductUseCase (Port IN)      │ ProductRepository (Port OUT)
│  ┌────────▼──────────────────────────────▼───────────┐ │
│  │                APLICAÇÃO                           │ │
│  │                ProductService                      │ │
│  └──────────────────────┬────────────────────────────┘ │
│                         │                               │
│  ┌──────────────────────▼────────────────────────────┐ │
│  │                  DOMÍNIO                           │ │
│  │  Product · ProductName · Money · CategoryId        │ │
│  │  ProductStatus · Exceptions                        │ │
│  │       *** ZERO dependências de framework ***       │ │
│  └───────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────┘
```

Diagramas C4 completos em [`docs/architecture/ARCHITECTURE.md`](docs/architecture/ARCHITECTURE.md)

## Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Core | Java 21 · Spring Boot 3.3.4 |
| Persistência | Spring Data JPA · H2 (testes) · PostgreSQL 16 (produção) |
| Testes | JUnit 5 · Mockito · ArchUnit 1.3 · @DataJpaTest |
| Docs | SpringDoc OpenAPI 2.5 · Swagger UI |
| Infraestrutura | Docker · Docker Compose · GitHub Actions |
| Qualidade | JaCoCo (≥ 70%) · Quality Gate customizado |

## Como Rodar

```bash
# Clone
git clone https://github.com/wesleytaumaturgo/spring-boot-hexagonal-clean-architecture.git
cd spring-boot-hexagonal-clean-architecture

# Com Docker Compose (recomendado)
docker-compose up -d
# Acesse: http://localhost:8080/swagger-ui.html

# Apenas testes
./mvnw test

# Quality gate
bash scripts/run-gate.sh
```

**Variáveis de ambiente** — copie `.env.example` para `.env.local` se necessário.

## API Endpoints

| Método | Caminho | Descrição | Resposta |
|--------|---------|-----------|----------|
| `POST` | `/api/v1/products` | Criar produto | 201 / 409 / 422 |
| `GET` | `/api/v1/products/{id}` | Buscar por ID | 200 / 404 |
| `GET` | `/api/v1/products` | Listar (paginado) | 200 |
| `PUT` | `/api/v1/products/{id}` | Atualizar | 200 / 404 / 422 |
| `PATCH` | `/api/v1/products/{id}/deactivate` | Desativar | 200 / 404 / 409 |

Swagger UI disponível em `/swagger-ui.html` com a aplicação rodando.

## Estrutura do Projeto

```
src/main/java/com/example/hexagonal/
├── HexagonalApplication.java
├── domain/                     ← Java puro — ZERO framework
│   ├── model/                  (Product, ProductStatus)
│   ├── valueobject/            (ProductName, Money, CategoryId)
│   ├── port/in/                (ProductUseCase)
│   ├── port/out/               (ProductRepository)
│   └── exception/
├── application/                ← Casos de uso
│   └── ProductService.java
└── infrastructure/             ← Adapters e configuração
    ├── adapter/in/rest/        (ProductController, GlobalExceptionHandler, DTOs)
    ├── adapter/out/persistence/(ProductPersistenceAdapter, JPA entity)
    └── config/                 (BeanConfig — wiring)
```

## TDD Cycle

Este projeto foi construído via TDD estrito (RED → GREEN → REFACTOR) em 3 branches:

| Branch | Escopo | Testes |
|--------|--------|--------|
| `feat/core-domain` | Value Objects, Product, exceções | 27 |
| `feat/core-application` | ProductService, ArchUnit | 15 |
| `feat/core-adapters-infra` | Controller, JPA, smoke test | 17 |

Cada branch tem commits separados de RED (testes falhando), GREEN (implementação mínima) e REFACTOR (limpeza).

## Contexto

Arquitetura Hexagonal e Clean Architecture são temas centrais
do MBA em Arquitetura de Software (Full Cycle, 2024-2026).
Este projeto traduz esses princípios em código funcional e testável,
aplicando o que utilizo diariamente na modelagem de microsserviços
na Sem Parar (Grupo Corpay).

## English

REST API built with Hexagonal Architecture (Ports & Adapters).
Domain layer has zero framework dependencies — enforced by ArchUnit at build time.
59 tests · 95% line coverage · Java 21 + Spring Boot 3.3.
Run: `docker-compose up -d` then `./mvnw test`.

## Licença

MIT
