package com.wesleytaumaturgo.hexagonal.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

// REQ-1.EARS-2, REQ-1.EARS-4
class ProductNameTest {

    @Test
    void shouldCreateProductNameWhenValueIsValid() {
        ProductName name = new ProductName("Notebook Dell");
        assertThat(name.getValue()).isEqualTo("Notebook Dell");
    }

    @Test
    void shouldTrimLeadingAndTrailingWhitespace() {
        ProductName name = new ProductName("  Notebook  ");
        assertThat(name.getValue()).isEqualTo("Notebook");
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        assertThatThrownBy(() -> new ProductName(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ProductName must not be blank");
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        assertThatThrownBy(() -> new ProductName("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ProductName must not be blank");
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {
        assertThatThrownBy(() -> new ProductName(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ProductName must not be blank");
    }

    @Test
    void shouldThrowWhenNameExceeds100Characters() {
        String longName = "A".repeat(101);
        assertThatThrownBy(() -> new ProductName(longName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ProductName must not exceed 100 characters");
    }

    @Test
    void shouldAcceptNameWith100Characters() {
        String name100 = "A".repeat(100);
        assertThatNoException().isThrownBy(() -> new ProductName(name100));
    }

    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        ProductName a = new ProductName("Notebook");
        ProductName b = new ProductName("Notebook");
        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }
}
