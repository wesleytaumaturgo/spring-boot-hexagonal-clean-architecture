package com.example.hexagonal.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "ProductName must not be blank")
        @Size(max = 100, message = "ProductName must not exceed 100 characters")
        String name,

        @NotNull(message = "Price must not be null")
        @Positive(message = "Money amount must be positive")
        BigDecimal price,

        @NotBlank(message = "CategoryId must not be null")
        String categoryId
) {}
