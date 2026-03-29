package com.example.hexagonal.infrastructure.adapter.out.persistence;

import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.model.ProductStatus;
import com.example.hexagonal.domain.port.out.ProductRepository;
import com.example.hexagonal.domain.valueobject.CategoryId;
import com.example.hexagonal.domain.valueobject.Money;
import com.example.hexagonal.domain.valueobject.ProductName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

// REQ-1, REQ-2, REQ-3, REQ-5
@DataJpaTest
@Import(ProductPersistenceAdapter.class)
class ProductRepositoryIT {

    @Autowired
    private ProductRepository productRepository;

    private final CategoryId categoryId = new CategoryId(UUID.randomUUID());
    private final ProductName name = new ProductName("Notebook Dell");
    private final Money price = new Money(new BigDecimal("4999.99"));

    @Test
    void shouldSaveAndFindById() {
        // REQ-1, REQ-2
        Product saved = productRepository.save(Product.create(name, price, categoryId));

        Optional<Product> found = productRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo(name);
        assertThat(found.get().getStatus()).isEqualTo(ProductStatus.ACTIVE);
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {
        // REQ-2.EARS-2
        Optional<Product> found = productRepository.findById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAllWithPagination() {
        // REQ-3.EARS-1
        productRepository.save(Product.create(name, price, categoryId));
        productRepository.save(Product.create(new ProductName("Tablet Samsung"), new Money(new BigDecimal("1999.99")), categoryId));

        Page<Product> page = productRepository.findAll(null, null, PageRequest.of(0, 10));

        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldFilterByCategoryId() {
        // REQ-3.EARS-2
        CategoryId otherCategory = new CategoryId(UUID.randomUUID());
        productRepository.save(Product.create(name, price, categoryId));
        productRepository.save(Product.create(new ProductName("Mouse Logitech"), new Money(new BigDecimal("199.99")), otherCategory));

        Page<Product> page = productRepository.findAll(categoryId, null, PageRequest.of(0, 10));

        assertThat(page.getContent()).allMatch(p -> p.getCategoryId().equals(categoryId));
    }

    @Test
    void shouldFilterByStatus() {
        // REQ-3.EARS-3
        Product product = productRepository.save(Product.create(name, price, categoryId));
        product.deactivate();
        productRepository.save(product);

        Page<Product> active = productRepository.findAll(null, ProductStatus.ACTIVE, PageRequest.of(0, 10));
        Page<Product> inactive = productRepository.findAll(null, ProductStatus.INACTIVE, PageRequest.of(0, 10));

        assertThat(inactive.getContent()).anyMatch(p -> p.getId().equals(product.getId()));
        assertThat(active.getContent()).noneMatch(p -> p.getId().equals(product.getId()));
    }

    @Test
    void shouldDetectDuplicateByNameAndCategory() {
        // REQ-1.EARS-6
        productRepository.save(Product.create(name, price, categoryId));

        boolean exists = productRepository.existsByNameAndCategoryId(name.getValue(), categoryId.getValue());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenNoMatch() {
        boolean exists = productRepository.existsByNameAndCategoryId("Non-existent", UUID.randomUUID());

        assertThat(exists).isFalse();
    }
}
