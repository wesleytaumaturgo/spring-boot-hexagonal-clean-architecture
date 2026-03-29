package com.example.hexagonal.domain.exception;

import java.util.UUID;

public class ProductAlreadyInactiveException extends RuntimeException {
    public ProductAlreadyInactiveException(UUID id) {
        super("Product already inactive: " + id);
    }
}
