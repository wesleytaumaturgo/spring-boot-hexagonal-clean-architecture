# ADR-003: H2 In-Memory como Banco de Dados de Testes

**Status:** Aceito
**Data:** 2026-03-29

## Contexto

Os testes de integração da camada de persistência precisam de um banco de dados real para
executar consultas JPA. Usar o PostgreSQL de produção nos testes não é adequado. Precisamos
de uma alternativa que seja rápida, sem dependências externas e compatível com o esquema.

## Decisão

Utilizar o banco H2 in-memory em `MODE=PostgreSQL` para todos os testes com `@DataJpaTest`
e `@SpringBootTest`.

Configuração em `src/test/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:hexagonal;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
```

## Alternativas Consideradas

| Alternativa | Motivo da Rejeição |
|---|---|
| Testcontainers (PostgreSQL) | Exige Docker no CI; inicialização mais lenta; excessivo para consultas CRUD simples |
| PostgreSQL embarcado | Dependência mais pesada; tempo de build maior |
| Mockar o repositório | Derrota o propósito dos testes de integração de persistência |

## Consequências

**Positivas:**
- ✅ Testes iniciam em menos de 1 segundo (sem container)
- ✅ Sem dependências externas no CI
- ✅ Modo de compatibilidade H2/PostgreSQL cobre o SQL padrão do projeto

**Negativas:**
- ⚠️ H2 não é 100% compatível com PostgreSQL (algumas funções específicas diferem)
- ⚠️ Se consultas específicas do PostgreSQL forem adicionadas, será necessário migrar para Testcontainers

## Quando Revisar

Migrar para Testcontainers quando o projeto usar funcionalidades específicas do PostgreSQL:
colunas JSON, arrays, full-text search ou advisory locks.

## Referências

- `src/test/resources/application.yml`
- `ProductRepositoryIT.java`
