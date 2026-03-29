package com.example.hexagonal.domain.exception;

import java.util.UUID;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(UUID categoryId) {
        super("Product already exists in category: " + categoryId);
    }
}
