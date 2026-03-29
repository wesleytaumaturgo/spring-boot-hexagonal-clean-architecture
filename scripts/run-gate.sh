#!/usr/bin/env bash
# Quality Gate — D1 (cobertura) + D3 (segurança) + D5 (documentação)
# Uso: bash scripts/run-gate.sh [TASK-ID]

set -euo pipefail

TASK_ID="${1:-}"
PASS=0
FAIL=0
RESULTS=()

log_pass() { PASS=$((PASS+1)); RESULTS+=("✅ $1"); }
log_fail() { FAIL=$((FAIL+1)); RESULTS+=("❌ $1"); }
log_warn() { RESULTS+=("⚠️  $1"); }

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Quality Gate${TASK_ID:+ — $TASK_ID}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# ────────────────────────────────────────────
# D1 — Cobertura (JaCoCo)
# ────────────────────────────────────────────
echo ""
echo "D1 — Cobertura de Testes"

JACOCO_REPORT="target/site/jacoco/index.html"
THRESHOLD=70

if [ ! -f "$JACOCO_REPORT" ]; then
    log_warn "D1: Relatório JaCoCo não encontrado. Execute: mvn test jacoco:report"
    RESULTS+=("   → Pulando verificação de cobertura")
else
    COVERED=$(grep -oP 'Total[^%]+?(\d+)%' "$JACOCO_REPORT" 2>/dev/null | grep -oP '\d+' | tail -1 || echo "0")
    if [ "$COVERED" -ge "$THRESHOLD" ] 2>/dev/null; then
        log_pass "D1: Cobertura ${COVERED}% ≥ ${THRESHOLD}%"
    else
        log_fail "D1: Cobertura ${COVERED}% < ${THRESHOLD}% (mínimo exigido)"
    fi
fi

# ────────────────────────────────────────────
# D3 — Segurança (OWASP básico)
# ────────────────────────────────────────────
echo ""
echo "D3 — Segurança"

# Hardcoded secrets check
HARDCODED=$(grep -rn \
    -e 'password\s*=\s*"[^$][^"]\+\+"' \
    -e 'secret\s*=\s*"[^$][^"]\+\+"' \
    -e 'api.key\s*=\s*"[^"]\+\+"' \
    src/main/ 2>/dev/null | grep -v "changeme\|testpass\|example" || true)

if [ -z "$HARDCODED" ]; then
    log_pass "D3: Sem secrets hardcoded detectados"
else
    log_fail "D3: Secrets hardcoded detectados:"
    echo "$HARDCODED" | head -5
fi

# SQL injection check — concatenação com variáveis (não strings literais)
# Ignora: strings literais em @Query e continuação de linha (ex: "... " + "...")
SQL_INJECT=$(grep -rn '+ \w\+\b\|+ [a-z][A-Za-z]*\b' \
    src/main/java/com/example/hexagonal \
    --include="*.java" 2>/dev/null \
    | grep -v '//' \
    | grep -iE '"(SELECT|INSERT|UPDATE|DELETE)' \
    | grep -v '@Query\|//\|"[^"]*"\s*\+\s*"' \
    || true)

if [ -z "$SQL_INJECT" ]; then
    log_pass "D3: Sem concatenação SQL com variáveis detectada"
else
    log_fail "D3: Possível SQL injection (concatenação com variável):"
    echo "$SQL_INJECT" | head -3
fi

# domain deve ser livre de framework
DOMAIN_SPRING=$(grep -rn '@Component\|@Service\|@Repository\|@Autowired\|@Inject' \
    src/main/java/com/example/hexagonal/domain/ 2>/dev/null || true)

if [ -z "$DOMAIN_SPRING" ]; then
    log_pass "D3: Domain layer sem annotations Spring (zero framework deps)"
else
    log_fail "D3: Domain layer tem annotations Spring:"
    echo "$DOMAIN_SPRING" | head -5
fi

# ────────────────────────────────────────────
# D5 — Documentação (ADR + Spec)
# ────────────────────────────────────────────
echo ""
echo "D5 — Documentação"

ADR_COUNT=$(find docs/adr/records -name "ADR-*.md" 2>/dev/null | wc -l)
if [ "$ADR_COUNT" -gt 0 ]; then
    log_pass "D5: $ADR_COUNT ADR(s) encontrado(s)"
else
    log_warn "D5: Nenhum ADR encontrado em docs/adr/records/"
    RESULTS+=("   → Crie ADRs para decisões arquiteturais relevantes")
fi

SPEC_COUNT=$(find docs/specs -name "requirements.md" 2>/dev/null | wc -l)
if [ "$SPEC_COUNT" -gt 0 ]; then
    log_pass "D5: $SPEC_COUNT requirements.md encontrado(s)"
else
    log_fail "D5: Nenhum requirements.md em docs/specs/"
fi

# ────────────────────────────────────────────
# Resultado final
# ────────────────────────────────────────────
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "  Resultado"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

for r in "${RESULTS[@]}"; do
    echo "  $r"
done

echo ""
if [ "$FAIL" -eq 0 ]; then
    echo "  GATE PASS — $PASS checks OK | 0 falhas"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    exit 0
else
    echo "  GATE FAIL — $PASS checks OK | $FAIL falhas"
    echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    exit 1
fi
