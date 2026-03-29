package com.example.hexagonal.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public final class Money {

    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        // stub — sem validação (RED)
        this.amount = amount;
    }

    public Money(double amount) {
        this(BigDecimal.valueOf(amount));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money that)) return false;
        return Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
