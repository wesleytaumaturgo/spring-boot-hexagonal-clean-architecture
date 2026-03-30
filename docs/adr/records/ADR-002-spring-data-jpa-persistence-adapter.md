# ADR-002: Spring Data JPA como Adapter de Persistência

**Status:** Aceito
**Data:** 2026-03-29

## Contexto

A camada de domínio define a interface `ProductRepository` (port de saída) sem nenhuma
suposição sobre tecnologia de persistência. Precisamos escolher como implementar esse port
na camada de infraestrutura, mantendo o isolamento do domínio.

## Decisão

Utilizar Spring Data JPA com uma classe intermediária `ProductPersistenceAdapter` que:

1. Implementa o port `ProductRepository` (interface do domínio)
2. Delega para um `SpringDataProductRepository` com visibilidade de pacote (package-private)
3. Realiza o mapeamento entre objetos de domínio (`Product`) e entidades JPA (`ProductJpaEntity`)

O `SpringDataProductRepository` é intencionalmente package-private — é detalhe de
implementação da camada de infraestrutura e não deve ser acessível pelo domínio ou pela
camada de aplicação.

## Alternativas Consideradas

| Alternativa | Motivo da Rejeição |
|---|---|
| `@Repository` diretamente no port do domínio | Viola a arquitetura hexagonal — domínio dependeria de Spring |
| MyBatis | Mais boilerplate para operações CRUD; menor integração com o ecossistema Spring Boot |
| JOOQ | Melhor para consultas complexas, mas excessivo para este caso de uso CRUD |
| JDBC puro | Sem mapeamento ORM; maior custo de manutenção |

## Consequências

**Positivas:**
- ✅ Camada de domínio livre de annotations de persistência
- ✅ O port `ProductRepository` pode ser trocado (ex: adapter NoSQL) sem tocar no domínio ou na aplicação
- ✅ `@DataJpaTest` permite testes de persistência rápidos e isolados com H2

**Negativas:**
- ⚠️ Mapeamento em duas camadas (domínio ↔ entidade JPA) adiciona boilerplate
- ⚠️ Proxies JPA podem causar problemas com Value Objects se não forem mapeados em tipos primitivos

## Referências

- ADR-001: Decisão pela Arquitetura Hexagonal
- `ProductPersistenceAdapter.java` — implementação na infraestrutura
- `ProductRepository.java` — port de saída no domínio
