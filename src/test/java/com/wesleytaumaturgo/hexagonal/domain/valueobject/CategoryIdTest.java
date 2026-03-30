package com.wesleytaumaturgo.hexagonal.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

// REQ-1.EARS-5
class CategoryIdTest {

    @Test
    void shouldCreateCategoryIdFromUUID() {
        UUID uuid = UUID.randomUUID();
        CategoryId id = new CategoryId(uuid);
        assertThat(id.getValue()).isEqualTo(uuid);
    }

    @Test
    void shouldCreateCategoryIdFromValidUUIDString() {
        String uuidStr = "550e8400-e29b-41d4-a716-446655440000";
        CategoryId id = CategoryId.of(uuidStr);
        assertThat(id.getValue().toString()).isEqualTo(uuidStr);
    }

    @Test
    void shouldThrowWhenUUIDIsNull() {
        assertThatThrownBy(() -> new CategoryId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CategoryId must be a valid UUID");
    }

    @Test
    void shouldThrowWhenUUIDStringIsInvalid() {
        assertThatThrownBy(() -> CategoryId.of("not-a-valid-uuid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CategoryId must be a valid UUID");
    }

    @Test
    void shouldThrowWhenUUIDStringIsBlank() {
        assertThatThrownBy(() -> CategoryId.of(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CategoryId must be a valid UUID");
    }

    @Test
    void shouldThrowWhenUUIDStringIsNull() {
        assertThatThrownBy(() -> CategoryId.of(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("CategoryId must be a valid UUID");
    }

    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        UUID uuid = UUID.randomUUID();
        CategoryId a = new CategoryId(uuid);
        CategoryId b = new CategoryId(uuid);
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
