package com.example.hexagonal.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public final class CategoryId {

    private final UUID value;

    public CategoryId(UUID value) {
        // stub — sem validação (RED)
        this.value = value;
    }

    public static CategoryId of(String uuidString) {
        // stub — sem validação (RED)
        if (uuidString == null || uuidString.isBlank()) return new CategoryId(null);
        return new CategoryId(UUID.fromString(uuidString));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryId that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
