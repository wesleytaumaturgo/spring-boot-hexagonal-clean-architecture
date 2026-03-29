package com.example.hexagonal.domain.valueobject;

import java.util.Objects;

public final class ProductName {

    private static final int MAX_LENGTH = 100;

    private final String value;

    public ProductName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductName must not be blank");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("ProductName must not exceed 100 characters");
        }
        this.value = value.trim();
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

    @Override
    public String toString() {
        return value;
    }
}
