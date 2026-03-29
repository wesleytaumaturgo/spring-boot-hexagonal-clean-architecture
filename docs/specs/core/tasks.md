---
status: APPROVED
audit_score: 9/10
audit_date: 2026-03-29
---

# Tasks — BC Core: Gestão de Produtos

## Resumo

| Tier | Tasks | Estimativa |
|------|-------|-----------|
| T1 Domain | TASK-001 a TASK-004 | 4h |
| T1 Application | TASK-005 a TASK-006 | 3h |
| T2 Infrastructure | TASK-007 a TASK-010 | 4h |
| Architecture | TASK-011 a TASK-012 | 1.5h |
| Refactor | TASK-013 | 1h |
| **Total** | **13 tasks** | **~13.5h** |

---

## Fase 1 — Domain Layer (T1)

### TASK-001 — [RED] Testes de Value Objects
- **Tipo:** test
- **Tier:** T1
- **Spec:** REQ-1.EARS-2, REQ-1.EARS-3, REQ-1.EARS-4, REQ-1.EARS-5
- **Arquivos:**
  - `src/test/java/com/example/hexagonal/domain/valueobject/ProductNameTest.java`
  - `src/test/java/com/example/hexagonal/domain/valueobject/MoneyTest.java`
  - `src/test/java/com/example/hexagonal/domain/valueobject/CategoryIdTest.java`
- **Estimativa:** 1h
- **Deps:** nenhuma

### TASK-002 — [GREEN] Implementar Value Objects
- **Tipo:** feat
- **Tier:** T1
- **Spec:** REQ-1.EARS-2, REQ-1.EARS-3, REQ-1.EARS-4, REQ-1.EARS-5
- **Arquivos:**
  - `src/main/java/com/example/hexagonal/domain/valueobject/ProductName.java`
  - `src/main/java/com/example/hexagonal/domain/valueobject/Money.java`
  - `src/main/java/com/example/hexagonal/domain/valueobject/CategoryId.java`
- **Estimativa:** 1h
- **Deps:** TASK-001

### TASK-003 — [RED] Testes da entidade Product
- **Tipo:** test
- **Tier:** T1
- **Spec:** REQ-5.EARS-1, REQ-5.EARS-2
- **Arquivos:**
  - `src/test/java/com/example/hexagonal/domain/model/ProductTest.java`
- **Estimativa:** 1h
- **Deps:** TASK-002

### TASK-004 — [GREEN] Implementar entidade Product
- **Tipo:** feat
- **Tier:** T1
- **Spec:** REQ-5.EARS-1, REQ-5.EARS-2
- **Arquivos:**
  - `src/main/java/com/example/hexagonal/domain/model/Product.java`
  - `src/main/java/com/example/hexagonal/domain/model/ProductStatus.java`
  - `src/main/java/com/example/hexagonal/domain/port/in/ProductUseCase.java`
  - `src/main/java/com/example/hexagonal/domain/port/out/ProductRepository.java`
  - `src/main/java/com/example/hexagonal/domain/exception/ProductNotFoundException.java`
  - `src/main/java/com/example/hexagonal/domain/exception/ProductAlreadyExistsException.java`
  - `src/main/java/com/example/hexagonal/domain/exception/ProductAlreadyInactiveException.java`
- **Estimativa:** 1h
- **Deps:** TASK-003

---

## Fase 2 — Application Layer (T1)

### TASK-005 — [RED] Testes de ProductService
- **Tipo:** test
- **Tier:** T1
- **Spec:** REQ-1.EARS-1, REQ-1.EARS-6, REQ-2.EARS-1, REQ-2.EARS-2, REQ-3.EARS-1, REQ-3.EARS-2, REQ-3.EARS-3, REQ-4.EARS-1, REQ-4.EARS-2, REQ-5.EARS-3
- **Arquivos:**
  - `src/test/java/com/example/hexagonal/application/ProductServiceTest.java`
- **Estimativa:** 1.5h
- **Deps:** TASK-004

### TASK-006 — [GREEN] Implementar ProductService
- **Tipo:** feat
- **Tier:** T1
- **Spec:** REQ-1.EARS-1, REQ-2.EARS-1, REQ-3.EARS-1, REQ-4.EARS-1, REQ-5.EARS-1
- **Arquivos:**
  - `src/main/java/com/example/hexagonal/application/ProductService.java`
- **Estimativa:** 1.5h
- **Deps:** TASK-005

---

## Fase 3 — Infrastructure Layer (T2)

### TASK-007 — [RED] Testes do ProductController
- **Tipo:** test
- **Tier:** T2
- **Spec:** REQ-1.EARS-1, REQ-2.EARS-1, REQ-3.EARS-1, REQ-4.EARS-1, REQ-5.EARS-1
- **Arquivos:**
  - `src/test/java/com/example/hexagonal/infrastructure/adapter/in/rest/ProductControllerTest.java`
- **Estimativa:** 1.5h
- **Deps:** TASK-006

### TASK-008 — [GREEN] Implementar ProductController e DTOs
- **Tipo:** feat
- **Tier:** T2
- **Spec:** REQ-1 a REQ-5 (contrato REST completo)
- **Arquivos:**
  - `src/main/java/com/example/hexagonal/infrastructure/adapter/in/rest/ProductController.java`
  - `src/main/java/com/example/hexagonal/infrastructure/adapter/in/rest/dto/CreateProductRequest.java`
  - `src/main/java/com/example/hexagonal/infrastructure/adapter/in/rest/dto/UpdateProductRequest.java`
  - `src/main/java/com/example/hexagonal/infrastructure/adapter/in/rest/dto/ProductResponse.java`
  - `src/main/java/com/example/hexagonal/infrastructure/config/GlobalExceptionHandler.java`
- **Estimativa:** 1.5h
- **Deps:** TASK-007

### TASK-009 — [RED] Testes de integração do ProductPersistenceAdapter
- **Tipo:** test
- **Tier:** T2 (Testcontainers)
- **Spec:** REQ-2.EARS-1, REQ-3.EARS-1, REQ-1.EARS-6
- **Arquivos:**
  - `src/test/java/com/example/hexagonal/integration/ProductRepositoryIT.java`
- **Estimativa:** 1h
- **Deps:** TASK-004

### TASK-010 — [GREEN] Implementar ProductPersistenceAdapter
- **Tipo:** feat
- **Tier:** T2
- **Spec:** REQ-2.EARS-1, REQ-3.EARS-1, REQ-1.EARS-6
- **Arquivos:**
  - `src/main/java/com/example/hexagonal/infrastructure/adapter/out/persistence/ProductJpaEntity.java`
  - `src/main/java/com/example/hexagonal/infrastructure/adapter/out/persistence/SpringDataProductRepository.java`
  - `src/main/java/com/example/hexagonal/infrastructure/adapter/out/persistence/ProductPersistenceAdapter.java`
  - `src/main/java/com/example/hexagonal/infrastructure/config/BeanConfig.java`
  - `src/main/resources/application.yml`
- **Estimativa:** 1.5h
- **Deps:** TASK-009

---

## Fase 4 — ArchUnit

### TASK-011 — [RED] Testes ArchUnit (boundary enforcement)
- **Tipo:** test
- **Spec:** REQ-6.EARS-1, REQ-6.EARS-2, REQ-6.EARS-3
- **Arquivos:**
  - `src/test/java/com/example/hexagonal/architecture/ArchUnitTest.java`
- **Estimativa:** 45min
- **Deps:** TASK-010

### TASK-012 — [GREEN] Validar que ArchUnit passa
- **Tipo:** chore
- **Spec:** REQ-6.EARS-1, REQ-6.EARS-2, REQ-6.EARS-3
- **Estimativa:** 45min
- **Deps:** TASK-011
- **Nota:** Corrigir violações se ArchUnit falhar

---

## Fase 5 — Refactor

### TASK-013 — [REFACTOR] Cleanup final e cobertura
- **Tipo:** refactor
- **Spec:** NFR-2 (cobertura ≥ 70%), NFR-3 (zero violações ArchUnit)
- **Estimativa:** 1h
- **Deps:** TASK-006, TASK-008, TASK-010, TASK-012
- **Ações:**
  - Remover código duplicado
  - Validar cobertura JaCoCo ≥ 70%
  - Revisar mensagens de erro
  - Atualizar OpenAPI descriptions
