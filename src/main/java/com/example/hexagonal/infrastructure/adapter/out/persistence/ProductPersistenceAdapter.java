package com.example.hexagonal.infrastructure.adapter.out.persistence;

import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.model.ProductStatus;
import com.example.hexagonal.domain.port.out.ProductRepository;
import com.example.hexagonal.domain.valueobject.CategoryId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProductPersistenceAdapter implements ProductRepository {

    private final SpringDataProductRepository springDataRepo;

    public ProductPersistenceAdapter(SpringDataProductRepository springDataRepo) {
        this.springDataRepo = springDataRepo;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = ProductJpaEntity.from(product);
        return springDataRepo.save(entity).toDomain();
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return springDataRepo.findById(id).map(ProductJpaEntity::toDomain);
    }

    @Override
    public Page<Product> findAll(CategoryId categoryId, ProductStatus status, Pageable pageable) {
        UUID catId = categoryId != null ? categoryId.getValue() : null;
        return springDataRepo.findAllFiltered(catId, status, pageable)
                .map(ProductJpaEntity::toDomain);
    }

    @Override
    public boolean existsByNameAndCategoryId(String name, UUID categoryId) {
        return springDataRepo.existsByNameAndCategoryId(name, categoryId);
    }
}
