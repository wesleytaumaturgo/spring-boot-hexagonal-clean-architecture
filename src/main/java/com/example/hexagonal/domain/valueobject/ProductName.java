package com.example.hexagonal.domain.valueobject;

import java.util.Objects;

public final class ProductName {

    private final String value;

    public ProductName(String value) {
        // stub — sem validação (RED)
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductName that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
