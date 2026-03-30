package com.wesleytaumaturgo.hexagonal.application;

import com.wesleytaumaturgo.hexagonal.domain.exception.ProductAlreadyExistsException;
import com.wesleytaumaturgo.hexagonal.domain.exception.ProductNotFoundException;
import com.wesleytaumaturgo.hexagonal.domain.model.Product;
import com.wesleytaumaturgo.hexagonal.domain.model.ProductStatus;
import com.wesleytaumaturgo.hexagonal.domain.port.out.ProductRepository;
import com.wesleytaumaturgo.hexagonal.domain.valueobject.CategoryId;
import com.wesleytaumaturgo.hexagonal.domain.valueobject.Money;
import com.wesleytaumaturgo.hexagonal.domain.valueobject.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

// REQ-1.EARS-1, REQ-1.EARS-6, REQ-2, REQ-3, REQ-4, REQ-5.EARS-3
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    private ProductName name;
    private Money price;
    private CategoryId categoryId;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
        name = new ProductName("Notebook Dell");
        price = new Money(new BigDecimal("4999.99"));
        categoryId = new CategoryId(UUID.randomUUID());
    }

    // ─── createProduct ────────────────────────────────────────────────────────

    @Test
    void shouldCreateProductWhenInputIsValid() {
        // REQ-1.EARS-1
        Product product = Product.create(name, price, categoryId);
        given(productRepository.existsByNameAndCategoryId(name.getValue(), categoryId.getValue()))
                .willReturn(false);
        given(productRepository.save(any(Product.class))).willReturn(product);

        Product result = productService.createProduct(name, price, categoryId);

        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getStatus()).isEqualTo(ProductStatus.ACTIVE);
        then(productRepository).should().save(any(Product.class));
    }

    @Test
    void shouldThrowWhenProductAlreadyExistsInCategory() {
        // REQ-1.EARS-6
        given(productRepository.existsByNameAndCategoryId(name.getValue(), categoryId.getValue()))
                .willReturn(true);

        assertThatThrownBy(() -> productService.createProduct(name, price, categoryId))
                .isInstanceOf(ProductAlreadyExistsException.class)
                .hasMessageContaining("already exists in category");

        then(productRepository).should(never()).save(any());
    }

    // ─── findById ─────────────────────────────────────────────────────────────

    @Test
    void shouldReturnProductWhenIdExists() {
        // REQ-2.EARS-1
        UUID id = UUID.randomUUID();
        Product product = Product.create(name, price, categoryId);
        given(productRepository.findById(id)).willReturn(Optional.of(product));

        Product result = productService.findById(id);

        assertThat(result).isEqualTo(product);
    }

    @Test
    void shouldThrowProductNotFoundWhenIdDoesNotExist() {
        // REQ-2.EARS-2
        UUID id = UUID.randomUUID();
        given(productRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(id))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product not found: " + id);
    }

    // ─── listProducts ─────────────────────────────────────────────────────────

    @Test
    void shouldReturnPagedListWhenNoFilters() {
        // REQ-3.EARS-1
        var pageable = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(Product.create(name, price, categoryId)));
        given(productRepository.findAll(null, null, pageable)).willReturn(page);

        var result = productService.listProducts(null, null, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldFilterByCategoryId() {
        // REQ-3.EARS-2
        var pageable = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(Product.create(name, price, categoryId)));
        given(productRepository.findAll(categoryId, null, pageable)).willReturn(page);

        var result = productService.listProducts(categoryId, null, pageable);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldFilterByStatus() {
        // REQ-3.EARS-3
        var pageable = PageRequest.of(0, 20);
        var page = new PageImpl<>(List.of(Product.create(name, price, categoryId)));
        given(productRepository.findAll(null, ProductStatus.ACTIVE, pageable)).willReturn(page);

        var result = productService.listProducts(null, ProductStatus.ACTIVE, pageable);

        assertThat(result).hasSize(1);
    }

    // ─── updateProduct ────────────────────────────────────────────────────────

    @Test
    void shouldUpdateProductWhenExists() {
        // REQ-4.EARS-1
        UUID id = UUID.randomUUID();
        Product product = Product.create(name, price, categoryId);
        ProductName newName = new ProductName("Tablet Samsung");
        Money newPrice = new Money(new BigDecimal("1500.00"));

        given(productRepository.findById(id)).willReturn(Optional.of(product));
        given(productRepository.save(any(Product.class))).willReturn(product);

        Product result = productService.updateProduct(id, newName, newPrice, categoryId);

        assertThat(result.getName()).isEqualTo(newName);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentProduct() {
        // REQ-4.EARS-2
        UUID id = UUID.randomUUID();
        given(productRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(id, name, price, categoryId))
                .isInstanceOf(ProductNotFoundException.class);
    }

    // ─── deactivateProduct ────────────────────────────────────────────────────

    @Test
    void shouldThrowWhenDeactivatingNonExistentProduct() {
        // REQ-5.EARS-3
        UUID id = UUID.randomUUID();
        given(productRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deactivateProduct(id))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void shouldDeactivateProductWhenActive() {
        UUID id = UUID.randomUUID();
        Product product = Product.create(name, price, categoryId);
        given(productRepository.findById(id)).willReturn(Optional.of(product));
        given(productRepository.save(any(Product.class))).willReturn(product);

        productService.deactivateProduct(id);

        assertThat(product.getStatus()).isEqualTo(ProductStatus.INACTIVE);
        then(productRepository).should().save(product);
    }
}
