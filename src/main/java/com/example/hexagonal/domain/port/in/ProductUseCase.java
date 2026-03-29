package com.example.hexagonal.domain.port.in;

import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.model.ProductStatus;
import com.example.hexagonal.domain.valueobject.CategoryId;
import com.example.hexagonal.domain.valueobject.Money;
import com.example.hexagonal.domain.valueobject.ProductName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductUseCase {

    Product createProduct(ProductName name, Money price, CategoryId categoryId);

    Product findById(UUID id);

    Page<Product> listProducts(CategoryId categoryId, ProductStatus status, Pageable pageable);

    Product updateProduct(UUID id, ProductName name, Money price, CategoryId categoryId);

    Product deactivateProduct(UUID id);
}
