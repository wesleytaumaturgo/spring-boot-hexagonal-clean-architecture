package com.wesleytaumaturgo.hexagonal.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public final class CategoryId {

    private final UUID value;

    public CategoryId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("CategoryId must be a valid UUID");
        }
        this.value = value;
    }

    public static CategoryId of(String uuidString) {
        if (uuidString == null || uuidString.isBlank()) {
            throw new IllegalArgumentException("CategoryId must be a valid UUID");
        }
        try {
            return new CategoryId(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("CategoryId must be a valid UUID");
        }
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

    @Override
    public String toString() {
        return value.toString();
    }
}
