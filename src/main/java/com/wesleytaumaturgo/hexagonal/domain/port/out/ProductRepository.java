package com.wesleytaumaturgo.hexagonal.domain.port.out;

import com.wesleytaumaturgo.hexagonal.domain.model.Product;
import com.wesleytaumaturgo.hexagonal.domain.model.ProductStatus;
import com.wesleytaumaturgo.hexagonal.domain.valueobject.CategoryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    Page<Product> findAll(CategoryId categoryId, ProductStatus status, Pageable pageable);
    boolean existsByNameAndCategoryId(String name, UUID categoryId);
}
