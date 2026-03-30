# ADR-001 — Adoção de Arquitetura Hexagonal (Ports & Adapters)
Data: 2026-03-29 | Status: ACCEPTED

## Contexto

O projeto é um portfolio para demonstrar boas práticas de arquitetura de software em Java.
A necessidade era escolher um padrão arquitetural que:
- Separasse regras de negócio de detalhes de infraestrutura
- Fosse testável de forma isolada (sem framework)
- Fosse validável de forma automática (ArchUnit)
- Representasse práticas usadas em sistemas de produção (Grupo Corpay / Sem Parar)

## Decisão

Adotar **Arquitetura Hexagonal** (Ports & Adapters, Alistair Cockburn, 2005) com as seguintes
camadas:

```
domain/       → Entidades, Value Objects, Ports, Exceptions (Java puro)
application/  → Implementação dos casos de uso (depende só de domain)
infrastructure/ → Adapters REST, JPA, configs Spring (depende de framework)
```

**Regras obrigatórias:**
1. `domain` NÃO importa `infrastructure`
2. `domain` NÃO importa `application`
3. `domain` NÃO usa nenhuma annotation Spring
4. Todas as regras são verificadas por ArchUnit em toda build

## Alternativas Consideradas

| Alternativa | Motivo da Rejeição |
|-------------|-------------------|
| MVC tradicional (Controller → Service → Repository) | Domain model anêmico, regras espalhadas em Services |
| Clean Architecture (Uncle Bob) | Mais camadas de abstração desnecessárias para este porte |
| DDD tático completo (Aggregates, Events) | Sobrecarga para um portfolio de uma entidade |

## Consequências

**Positivas:**
- Domain layer 100% testável sem Spring Test
- Fronteiras verificadas automaticamente (zero regressão arquitetural)
- Troca de banco de dados não exige mudança no domínio
- Portfolio demonstra pattern relevante para entrevistas técnicas

**Negativas:**
- Mais arquivos que MVC simples (mappers manuais entre JPA entity e domain model)
- Curva de aprendizado para novos desenvolvedores

## Referências

- Alistair Cockburn — [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- Tom Hombergs — *Get Your Hands Dirty on Clean Architecture* (2019)
- ArchUnit — [archunit.org](https://www.archunit.org/)
