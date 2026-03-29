package com.example.hexagonal.infrastructure.adapter.in.rest.dto;

import com.example.hexagonal.domain.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        BigDecimal price,
        UUID categoryId,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getAmount(),
                product.getCategoryId().getValue(),
                product.getStatus().name(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
