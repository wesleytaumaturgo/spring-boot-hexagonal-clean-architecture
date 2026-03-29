package com.example.hexagonal.domain.model;

import com.example.hexagonal.domain.valueobject.CategoryId;
import com.example.hexagonal.domain.valueobject.Money;
import com.example.hexagonal.domain.valueobject.ProductName;

import java.time.LocalDateTime;
import java.util.UUID;

public class Product {

    private final UUID id;
    private ProductName name;
    private Money price;
    private CategoryId categoryId;
    private ProductStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product(UUID id, ProductName name, Money price, CategoryId categoryId,
                   ProductStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Product create(ProductName name, Money price, CategoryId categoryId) {
        LocalDateTime now = LocalDateTime.now();
        return new Product(UUID.randomUUID(), name, price, categoryId,
                ProductStatus.ACTIVE, now, now);
    }

    public void deactivate() {
        // stub — sem validação (RED): não lança exceção ainda
        this.status = ProductStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(ProductName name, Money price, CategoryId categoryId) {
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public ProductName getName() { return name; }
    public Money getPrice() { return price; }
    public CategoryId getCategoryId() { return categoryId; }
    public ProductStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
