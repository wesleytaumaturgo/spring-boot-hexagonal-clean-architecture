# API Hexagonal — Clean Architecture

> API REST com Arquitetura Hexagonal e Clean Architecture.
> A camada de domínio tem ZERO dependências de framework.
> Testes ArchUnit garantem as fronteiras.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green)
![Licença](https://img.shields.io/badge/Licen%C3%A7a-MIT-blue)
![CI](https://github.com/wesleytaumaturgo/spring-boot-hexagonal-clean-architecture/actions/workflows/ci.yml/badge.svg)

## Demo

```
✅ Regra ArchUnit: domain NÃO importa infrastructure  → PASSOU
✅ Regra ArchUnit: domain NÃO importa application     → PASSOU
✅ Regra ArchUnit: domain NÃO usa annotations Spring   → PASSOU
✅ 23 testes unitários                                 → PASSOU
✅ 8 testes de integração                              → PASSOU
```

<p align="center">
  <img src="docs/architecture.png" alt="Arquitetura Hexagonal" width="600" />
</p>

## Sobre

A maioria dos projetos Spring Boot mistura regras de negócio com
controllers e repositories. Este projeto demonstra a separação
radical: o core do negócio é Java puro — sem Spring, sem JPA,
sem annotations de framework.

Testes ArchUnit rodam em toda build e impedem que alguém quebre
as fronteiras arquiteturais, mesmo sem querer.

## Funcionalidades

- Separação completa Domain / Application / Infrastructure
- Domain layer 100% Java puro (zero dependências)
- ArchUnit validando fronteiras automaticamente no build
- Ports & Adapters com inversão de dependência real
- Testes unitários no domínio sem mocks de framework
- API documentada com Swagger/OpenAPI

## Arquitetura

```
┌──────────────────────────────────────────────┐
│              INFRAESTRUTURA                   │
│  ┌──────────┐  ┌───────────┐  ┌───────────┐ │
│  │REST      │  │JPA        │  │Client     │ │
│  │Controller│  │Repository │  │Externo    │ │
│  └────┬─────┘  └─────┬─────┘  └─────┬─────┘ │
│       │ implementa    │ implementa    │       │
├───────▼───────────────▼──────────────▼───────┤
│              APLICAÇÃO (Casos de Uso)         │
├──────────────────────────────────────────────┤
│              DOMÍNIO (Core)                   │
│  Entidades · Value Objects · Ports            │
│  *** ZERO dependências de frameworks ***     │
└──────────────────────────────────────────────┘
```

## Tecnologias

- **Core:** Java 21 · Spring Boot 3.3
- **Persistência:** JPA · H2 (dev) / Oracle (prod)
- **Testes:** JUnit 5 · Mockito · ArchUnit
- **Infra:** Docker · Docker Compose · GitHub Actions
- **Docs:** OpenAPI / Swagger

## Como Rodar

```bash
git clone https://github.com/wesleytaumaturgo/spring-boot-hexagonal-clean-architecture.git
cd spring-boot-hexagonal-clean-architecture
docker-compose up -d
./mvnw spring-boot:run
```

## Estrutura do Projeto

```
src/main/java/
├── domain/           # Java puro — SEM framework
│   ├── model/        # Entidades e Value Objects
│   ├── port/in/      # Ports de entrada (Use Cases)
│   └── port/out/     # Ports de saída (Repositórios)
├── application/      # Implementação dos Use Cases
└── infrastructure/   # Código dependente de framework
    ├── adapter/in/   # Controllers REST
    ├── adapter/out/  # Repositórios JPA
    └── config/       # Configuração Spring
```

## Contexto

Arquitetura Hexagonal e Clean Architecture são temas centrais
do MBA em Arquitetura de Software (Full Cycle, 2024-2026),
com módulos ministrados por referências como Robert C. Martin
(Uncle Bob) e Alistar Cockburn. Este projeto traduz esses
princípios em código funcional e testável, aplicando o que
utilizo diariamente na modelagem de microsserviços na Sem Parar
(Grupo Corpay).

## English

REST API using Hexagonal Architecture (Ports & Adapters) with
Clean Architecture. Domain layer has zero framework dependencies.
ArchUnit tests enforce architectural boundaries at build time.
Java 21 + Spring Boot 3. Run: `docker-compose up -d && ./mvnw spring-boot:run`.

## Licença

MIT
