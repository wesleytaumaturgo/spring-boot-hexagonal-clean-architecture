# Requirements — BC Core: Gestão de Produtos
Data: 2026-03-29 | Versão: 1.0 | EARS Score: 9/10

---

## REQ-1: Criação de Produto

### REQ-1.EARS-1
**When** a client sends `POST /api/v1/products` with valid `name`, `price`, and `categoryId`,
**the system shall** persist a new Product with status `ACTIVE` and return HTTP `201 Created`
with the created product in the response body.

### REQ-1.EARS-2
**When** a client sends `POST /api/v1/products` with a `name` that is empty or contains
only whitespace characters,
**the system shall** return HTTP `422 Unprocessable Entity` with error message
`"ProductName must not be blank"`.

### REQ-1.EARS-3
**When** a client sends `POST /api/v1/products` with a `price` less than or equal to zero,
**the system shall** return HTTP `422 Unprocessable Entity` with error message
`"Money amount must be positive"`.

### REQ-1.EARS-4
**When** a client sends `POST /api/v1/products` with a `name` exceeding 100 characters,
**the system shall** return HTTP `422 Unprocessable Entity` with error message
`"ProductName must not exceed 100 characters"`.

### REQ-1.EARS-5
**When** a client sends `POST /api/v1/products` with a `categoryId` that is not a valid
UUID format,
**the system shall** return HTTP `422 Unprocessable Entity` with error message
`"CategoryId must be a valid UUID"`.

### REQ-1.EARS-6
**When** a client sends `POST /api/v1/products` with a `name` that already exists in
the same `categoryId`,
**the system shall** return HTTP `409 Conflict` with error message
`"Product already exists in category: {categoryId}"`.

---

## REQ-2: Consulta de Produto por ID

### REQ-2.EARS-1
**When** a client sends `GET /api/v1/products/{id}` and a product with that `id` exists,
**the system shall** return HTTP `200 OK` with the product data including `id`, `name`,
`price`, `categoryId`, and `status`.

### REQ-2.EARS-2
**When** a client sends `GET /api/v1/products/{id}` and no product with that `id` exists,
**the system shall** return HTTP `404 Not Found` with error message
`"Product not found: {id}"`.

---

## REQ-3: Listagem de Produtos

### REQ-3.EARS-1
**When** a client sends `GET /api/v1/products` without query parameters,
**the system shall** return HTTP `200 OK` with a paginated list of all products,
defaulting to `page=0` and `size=20`.

### REQ-3.EARS-2
**When** a client sends `GET /api/v1/products?categoryId={uuid}`,
**the system shall** return HTTP `200 OK` with only products belonging to
the specified category, maintaining pagination defaults.

### REQ-3.EARS-3
**When** a client sends `GET /api/v1/products?status=ACTIVE`,
**the system shall** return HTTP `200 OK` with only products whose `status`
equals `ACTIVE`.

---

## REQ-4: Atualização de Produto

### REQ-4.EARS-1
**When** a client sends `PUT /api/v1/products/{id}` with valid `name`, `price`,
and `categoryId`, and the product exists,
**the system shall** update the product's name, price, and categoryId, and return
HTTP `200 OK` with the updated product data.

### REQ-4.EARS-2
**When** a client sends `PUT /api/v1/products/{id}` and no product with that `id` exists,
**the system shall** return HTTP `404 Not Found` with error message
`"Product not found: {id}"`.

### REQ-4.EARS-3
**When** a client sends `PUT /api/v1/products/{id}` with invalid data (e.g., blank name,
non-positive price),
**the system shall** return HTTP `422 Unprocessable Entity` with field-level
validation error details.

---

## REQ-5: Desativação de Produto

### REQ-5.EARS-1
**When** a client sends `PATCH /api/v1/products/{id}/deactivate` and the product has
status `ACTIVE`,
**the system shall** change the product status to `INACTIVE` and return HTTP `200 OK`
with the updated product data.

### REQ-5.EARS-2
**When** a client sends `PATCH /api/v1/products/{id}/deactivate` and the product already
has status `INACTIVE`,
**the system shall** return HTTP `409 Conflict` with error message
`"Product already inactive: {id}"`.

### REQ-5.EARS-3
**When** a client sends `PATCH /api/v1/products/{id}/deactivate` and no product with that
`id` exists,
**the system shall** return HTTP `404 Not Found` with error message
`"Product not found: {id}"`.

---

## REQ-6: Fronteiras Arquiteturais (ArchUnit)

### REQ-6.EARS-1
**The system shall** enforce at build time that no class in the `domain` package imports
any class from the `infrastructure` package, validated by ArchUnit.

### REQ-6.EARS-2
**The system shall** enforce at build time that no class in the `domain` package uses
any Spring Framework annotation (`@Component`, `@Service`, `@Repository`,
`@Autowired`, etc.), validated by ArchUnit.

### REQ-6.EARS-3
**The system shall** enforce at build time that no class in the `domain` package imports
any class from the `application` package, validated by ArchUnit.

---

## NFRs

### NFR-1 — Performance
**The system shall** respond to `GET /api/v1/products/{id}` in < 200ms at the 95th
percentile under normal load (up to 50 concurrent requests/second).

### NFR-2 — Cobertura de Testes
**The system shall** maintain line coverage ≥ 70% across all source files,
as reported by JaCoCo and enforced in the quality gate.

### NFR-3 — Separação Arquitetural
**The system shall** maintain zero ArchUnit rule violations in every build,
verified by the CI pipeline.

---

## Rastreabilidade

| REQ ID | SPEC §Seção | Arquivo de Teste |
|--------|------------|-----------------|
| REQ-1.EARS-1 | §Happy Path — Criar Produto | `application/ProductServiceTest.java` |
| REQ-1.EARS-2 | §Edge Cases | `domain/valueobject/ProductNameTest.java` |
| REQ-1.EARS-3 | §Edge Cases | `domain/valueobject/MoneyTest.java` |
| REQ-1.EARS-4 | §Edge Cases | `domain/valueobject/ProductNameTest.java` |
| REQ-1.EARS-5 | §Edge Cases | `domain/valueobject/CategoryIdTest.java` |
| REQ-1.EARS-6 | §Edge Cases | `application/ProductServiceTest.java` |
| REQ-2.EARS-1 | §Happy Path — Consultar Produto | `application/ProductServiceTest.java` |
| REQ-2.EARS-2 | §Error Behaviors | `application/ProductServiceTest.java` |
| REQ-3.EARS-1 | §Happy Path — Listar Produtos | `application/ProductServiceTest.java` |
| REQ-3.EARS-2 | §Happy Path — Listar Produtos | `application/ProductServiceTest.java` |
| REQ-3.EARS-3 | §Happy Path — Listar Produtos | `application/ProductServiceTest.java` |
| REQ-4.EARS-1 | §Happy Path — Atualizar Produto | `application/ProductServiceTest.java` |
| REQ-4.EARS-2 | §Error Behaviors | `application/ProductServiceTest.java` |
| REQ-4.EARS-3 | §Edge Cases | `application/ProductServiceTest.java` |
| REQ-5.EARS-1 | §Happy Path — Desativar Produto | `domain/model/ProductTest.java` |
| REQ-5.EARS-2 | §Edge Cases | `domain/model/ProductTest.java` |
| REQ-5.EARS-3 | §Error Behaviors | `application/ProductServiceTest.java` |
| REQ-6.EARS-1 | §Critérios de Aceitação | `architecture/ArchUnitTest.java` |
| REQ-6.EARS-2 | §Critérios de Aceitação | `architecture/ArchUnitTest.java` |
| REQ-6.EARS-3 | §Critérios de Aceitação | `architecture/ArchUnitTest.java` |
