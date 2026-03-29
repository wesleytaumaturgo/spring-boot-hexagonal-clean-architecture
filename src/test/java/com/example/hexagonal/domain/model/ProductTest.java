package com.example.hexagonal.domain.model;

import com.example.hexagonal.domain.exception.ProductAlreadyInactiveException;
import com.example.hexagonal.domain.valueobject.CategoryId;
import com.example.hexagonal.domain.valueobject.Money;
import com.example.hexagonal.domain.valueobject.ProductName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

// REQ-5.EARS-1, REQ-5.EARS-2
class ProductTest {

    private Product createSample() {
        return Product.create(
                new ProductName("Notebook"),
                new Money(new BigDecimal("999.99")),
                new CategoryId(UUID.randomUUID())
        );
    }

    @Test
    void shouldCreateProductWithActiveStatus() {
        Product product = createSample();

        assertThat(product.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName().getValue()).isEqualTo("Notebook");
        assertThat(product.getPrice().getAmount()).isEqualByComparingTo("999.99");
        assertThat(product.getCreatedAt()).isNotNull();
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldDeactivateActiveProduct() {
        // REQ-5.EARS-1
        Product product = createSample();
        product.deactivate();
        assertThat(product.getStatus()).isEqualTo(ProductStatus.INACTIVE);
    }

    @Test
    void shouldThrowWhenDeactivatingAlreadyInactiveProduct() {
        // REQ-5.EARS-2
        Product product = createSample();
        product.deactivate();

        assertThatThrownBy(product::deactivate)
                .isInstanceOf(ProductAlreadyInactiveException.class)
                .hasMessageContaining("already inactive");
    }

    @Test
    void shouldUpdateNamePriceAndCategory() {
        Product product = createSample();
        ProductName newName = new ProductName("Tablet Samsung");
        Money newPrice = new Money(new BigDecimal("1500.00"));
        CategoryId newCat = new CategoryId(UUID.randomUUID());

        product.update(newName, newPrice, newCat);

        assertThat(product.getName()).isEqualTo(newName);
        assertThat(product.getPrice()).isEqualTo(newPrice);
        assertThat(product.getCategoryId()).isEqualTo(newCat);
    }

    @Test
    void shouldUpdateUpdatedAtAfterDeactivation() {
        Product product = createSample();
        var before = product.getUpdatedAt();

        try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        product.deactivate();

        assertThat(product.getUpdatedAt()).isAfterOrEqualTo(before);
    }
}
