package com.example.hexagonal.infrastructure.adapter.out.persistence;

import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.model.ProductStatus;
import com.example.hexagonal.domain.valueobject.CategoryId;
import com.example.hexagonal.domain.valueobject.Money;
import com.example.hexagonal.domain.valueobject.ProductName;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products",
        uniqueConstraints = @UniqueConstraint(
                name = "products_name_category_uq",
                columnNames = {"name", "category_id"}
        ))
public class ProductJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "category_id", nullable = false, columnDefinition = "uuid")
    private UUID categoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ProductStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ProductJpaEntity() {}

    public static ProductJpaEntity from(Product product) {
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.id = product.getId();
        entity.name = product.getName().getValue();
        entity.price = product.getPrice().getAmount();
        entity.categoryId = product.getCategoryId().getValue();
        entity.status = product.getStatus();
        entity.createdAt = product.getCreatedAt();
        entity.updatedAt = product.getUpdatedAt();
        return entity;
    }

    public Product toDomain() {
        return new Product(
                id,
                new ProductName(name),
                new Money(price),
                new CategoryId(categoryId),
                status,
                createdAt,
                updatedAt
        );
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
}
