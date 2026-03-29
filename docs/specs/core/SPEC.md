# SPEC: Gestão de Produtos — BC Core
Data: 2026-03-29 | Versão: 1.0
Status: Draft

## Contexto

API REST de demonstração de Arquitetura Hexagonal (Ports & Adapters) com Spring Boot 3.3.
O BC Core implementa o CRUD de Produtos como caso de uso central para demonstrar
separação radical entre as camadas de Domínio, Aplicação e Infraestrutura.

A camada de domínio contém ZERO dependências de framework. Testes ArchUnit
garantem as fronteiras em toda build, impedindo regressões arquiteturais.

## Objetivos

- CRUD completo de Produtos via REST API documentada com OpenAPI
- Value Objects imutáveis: ProductName, Money, CategoryId
- Regras de negócio encapsuladas na entidade Product (sem anemic domain model)
- Fronteiras arquiteturais validadas por ArchUnit no CI
- Cobertura de testes ≥ 70% (JaCoCo)

## Não-Objetivos

- Autenticação ou autorização de usuários
- Gestão de estoque ou movimentações de produto
- Integração com sistemas externos de pagamento ou catálogo
- Multi-tenancy

## Comportamento Esperado

### Happy Path — Criar Produto

1. Cliente envia `POST /api/v1/products` com `name`, `price`, `categoryId`
2. Sistema valida `ProductName` (não vazio, máx 100 chars)
3. Sistema valida `Money` (positivo, precisão 2 casas decimais)
4. Sistema valida `CategoryId` (UUID válido)
5. Sistema verifica que não existe produto com mesmo nome na mesma categoria
6. Sistema persiste produto com status `ACTIVE`
7. Sistema retorna `201 Created` com produto criado

### Happy Path — Consultar Produto por ID

1. Cliente envia `GET /api/v1/products/{id}`
2. Sistema localiza produto pelo UUID
3. Sistema retorna `200 OK` com dados completos do produto

### Happy Path — Listar Produtos

1. Cliente envia `GET /api/v1/products` (filtros opcionais: `categoryId`, `status`)
2. Sistema aplica filtros e retorna lista paginada
3. Sistema retorna `200 OK`

### Happy Path — Atualizar Produto

1. Cliente envia `PUT /api/v1/products/{id}` com novos dados
2. Sistema valida VOs
3. Sistema atualiza nome, preço e categoria
4. Sistema retorna `200 OK` com produto atualizado

### Happy Path — Desativar Produto

1. Cliente envia `PATCH /api/v1/products/{id}/deactivate`
2. Produto tem status `ACTIVE`
3. Sistema muda status para `INACTIVE`
4. Sistema retorna `200 OK` com produto atualizado

### Edge Cases

| Cenário | Resultado esperado |
|---------|-------------------|
| `ProductName` com apenas espaços em branco | 422 "ProductName must not be blank" |
| `Money` com valor zero | 422 "Money amount must be positive" |
| `Money` com valor negativo | 422 "Money amount must be positive" |
| `CategoryId` com formato inválido (não UUID) | 422 "CategoryId must be a valid UUID" |
| `ProductName` com mais de 100 caracteres | 422 "ProductName must not exceed 100 characters" |
| Nome duplicado na mesma categoria | 409 "Product already exists in category" |
| Desativar produto já `INACTIVE` | 409 "Product already inactive: {id}" |

### Error Behaviors

| Situação | Status | Mensagem |
|----------|--------|---------|
| Produto não encontrado | 404 | `"Product not found: {id}"` |
| Dados de entrada inválidos | 422 | `"Validation failed: {field}: {reason}"` |
| Conflito de estado | 409 | `"Product already inactive: {id}"` |
| Nome duplicado na categoria | 409 | `"Product already exists in category: {categoryId}"` |
| Erro interno | 500 | `"Internal server error"` |

## Contratos de API

Ver `docs/specs/core/design.md` — seção API Endpoints.

## Artefatos a Produzir

### Domínio (Java puro, zero dependências)
- [ ] `src/main/java/com/example/hexagonal/domain/model/Product.java`
- [ ] `src/main/java/com/example/hexagonal/domain/model/ProductStatus.java`
- [ ] `src/main/java/com/example/hexagonal/domain/valueobject/ProductName.java`
- [ ] `src/main/java/com/example/hexagonal/domain/valueobject/Money.java`
- [ ] `src/main/java/com/example/hexagonal/domain/valueobject/CategoryId.java`
- [ ] `src/main/java/com/example/hexagonal/domain/port/in/ProductUseCase.java`
- [ ] `src/main/java/com/example/hexagonal/domain/port/out/ProductRepository.java`
- [ ] `src/main/java/com/example/hexagonal/domain/exception/ProductNotFoundException.java`
- [ ] `src/main/java/com/example/hexagonal/domain/exception/ProductAlreadyExistsException.java`
- [ ] `src/main/java/com/example/hexagonal/domain/exception/ProductAlreadyInactiveException.java`

### Aplicação
- [ ] `src/main/java/com/example/hexagonal/application/ProductService.java`

### Infraestrutura
- [ ] `src/main/java/com/example/hexagonal/infrastructure/adapter/in/rest/ProductController.java`
- [ ] `src/main/java/com/example/hexagonal/infrastructure/adapter/in/rest/dto/CreateProductRequest.java`
- [ ] `src/main/java/com/example/hexagonal/infrastructure/adapter/in/rest/dto/UpdateProductRequest.java`
- [ ] `src/main/java/com/example/hexagonal/infrastructure/adapter/in/rest/dto/ProductResponse.java`
- [ ] `src/main/java/com/example/hexagonal/infrastructure/adapter/out/persistence/ProductJpaEntity.java`
- [ ] `src/main/java/com/example/hexagonal/infrastructure/adapter/out/persistence/SpringDataProductRepository.java`
- [ ] `src/main/java/com/example/hexagonal/infrastructure/adapter/out/persistence/ProductPersistenceAdapter.java`
- [ ] `src/main/java/com/example/hexagonal/infrastructure/config/BeanConfig.java`
- [ ] `src/main/java/com/example/hexagonal/infrastructure/config/GlobalExceptionHandler.java`

### Testes
- [ ] `src/test/java/com/example/hexagonal/domain/valueobject/ProductNameTest.java`
- [ ] `src/test/java/com/example/hexagonal/domain/valueobject/MoneyTest.java`
- [ ] `src/test/java/com/example/hexagonal/domain/valueobject/CategoryIdTest.java`
- [ ] `src/test/java/com/example/hexagonal/domain/model/ProductTest.java`
- [ ] `src/test/java/com/example/hexagonal/application/ProductServiceTest.java`
- [ ] `src/test/java/com/example/hexagonal/infrastructure/adapter/in/rest/ProductControllerTest.java`
- [ ] `src/test/java/com/example/hexagonal/architecture/ArchUnitTest.java`
- [ ] `src/test/java/com/example/hexagonal/integration/ProductRepositoryIT.java`

## Critérios de Aceitação

- [ ] `POST /api/v1/products` cria produto e retorna `201 Created`
- [ ] `GET /api/v1/products/{id}` retorna produto com `200 OK`
- [ ] `GET /api/v1/products` retorna lista paginada com `200 OK`
- [ ] `PUT /api/v1/products/{id}` atualiza produto com `200 OK`
- [ ] `PATCH /api/v1/products/{id}/deactivate` muda status com `200 OK`
- [ ] Validações de VO retornam `422` com mensagem descritiva
- [ ] ArchUnit: `domain` não importa `infrastructure`
- [ ] ArchUnit: `domain` não importa `application`
- [ ] ArchUnit: `domain` não usa annotations Spring
- [ ] Cobertura de linhas ≥ 70% (JaCoCo)

## Dependências

Nenhuma dependência de outros Bounded Contexts.

## Perguntas em Aberto

Nenhuma.
