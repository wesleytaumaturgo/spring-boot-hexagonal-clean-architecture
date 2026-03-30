.PHONY: help build test coverage lint clean gate docker-up docker-down

MVN := $(shell command -v ./mvnw 2>/dev/null || echo mvn)

help: ## Mostra esta ajuda
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

build: ## Compila o projeto
	$(MVN) clean package -DskipTests

test: ## Roda todos os testes
	$(MVN) test

coverage: ## Gera relatório de cobertura (JaCoCo)
	$(MVN) test jacoco:report

lint: ## Verificação estática (Checkstyle)
	$(MVN) checkstyle:check

clean: ## Limpa artefatos de build
	$(MVN) clean

gate: ## Quality gate (D1+D3+D5)
	bash scripts/run-gate.sh

docker-up: ## Sobe PostgreSQL + app via Docker Compose
	docker-compose up -d

docker-down: ## Para e remove containers
	docker-compose down
