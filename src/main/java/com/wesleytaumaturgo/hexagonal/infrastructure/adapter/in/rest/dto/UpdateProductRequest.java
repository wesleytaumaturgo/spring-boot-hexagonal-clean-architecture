package com.wesleytaumaturgo.hexagonal.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductRequest(
        @NotBlank String name,
        @NotNull @Positive BigDecimal price,
        @NotNull UUID categoryId
) {}
