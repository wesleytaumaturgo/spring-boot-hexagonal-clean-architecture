package com.example.hexagonal.infrastructure.adapter.out.persistence;

import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.model.ProductStatus;
import com.example.hexagonal.domain.port.out.ProductRepository;
import com.example.hexagonal.domain.valueobject.CategoryId;
import com.example.hexagonal.domain.valueobject.Money;
import com.example.hexagonal.domain.valueobject.ProductName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProductPersistenceAdapter implements ProductRepository {

    private final SpringDataProductRepository jpaRepository;

    public ProductPersistenceAdapter(SpringDataProductRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = toEntity(product);
        ProductJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Page<Product> findAll(CategoryId categoryId, ProductStatus status, Pageable pageable) {
        UUID categoryUuid = categoryId != null ? categoryId.getValue() : null;
        return jpaRepository.findAllFiltered(categoryUuid, status, pageable).map(this::toDomain);
    }

    @Override
    public boolean existsByNameAndCategoryId(String name, UUID categoryId) {
        return jpaRepository.existsByNameAndCategoryId(name, categoryId);
    }

    private ProductJpaEntity toEntity(Product product) {
        return new ProductJpaEntity(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getAmount(),
                product.getCategoryId().getValue(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    private Product toDomain(ProductJpaEntity entity) {
        return new Product(
                entity.getId(),
                new ProductName(entity.getName()),
                new Money(entity.getPrice()),
                new CategoryId(entity.getCategoryId()),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
