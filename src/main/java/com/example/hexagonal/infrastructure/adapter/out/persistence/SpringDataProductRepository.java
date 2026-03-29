package com.example.hexagonal.infrastructure.adapter.out.persistence;

import com.example.hexagonal.domain.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

interface SpringDataProductRepository extends JpaRepository<ProductJpaEntity, UUID> {

    boolean existsByNameAndCategoryId(String name, UUID categoryId);

    @Query("SELECT p FROM ProductJpaEntity p WHERE " +
           "(:categoryId IS NULL OR p.categoryId = :categoryId) AND " +
           "(:status IS NULL OR p.status = :status)")
    Page<ProductJpaEntity> findAllFiltered(
            @Param("categoryId") UUID categoryId,
            @Param("status") ProductStatus status,
            Pageable pageable);
}
