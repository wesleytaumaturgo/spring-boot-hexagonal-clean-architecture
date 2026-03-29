package com.example.hexagonal.application;

import com.example.hexagonal.domain.exception.ProductAlreadyExistsException;
import com.example.hexagonal.domain.exception.ProductNotFoundException;
import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.model.ProductStatus;
import com.example.hexagonal.domain.port.in.ProductUseCase;
import com.example.hexagonal.domain.port.out.ProductRepository;
import com.example.hexagonal.domain.valueobject.CategoryId;
import com.example.hexagonal.domain.valueobject.Money;
import com.example.hexagonal.domain.valueobject.ProductName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(ProductName name, Money price, CategoryId categoryId) {
        if (productRepository.existsByNameAndCategoryId(name.getValue(), categoryId.getValue())) {
            throw new ProductAlreadyExistsException(categoryId.getValue());
        }
        Product product = Product.create(name, price, categoryId);
        return productRepository.save(product);
    }

    @Override
    public Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Page<Product> listProducts(CategoryId categoryId, ProductStatus status, Pageable pageable) {
        return productRepository.findAll(categoryId, status, pageable);
    }

    @Override
    public Product updateProduct(UUID id, ProductName name, Money price, CategoryId categoryId) {
        Product product = findById(id);
        product.update(name, price, categoryId);
        return productRepository.save(product);
    }

    @Override
    public Product deactivateProduct(UUID id) {
        Product product = findById(id);
        product.deactivate();
        return productRepository.save(product);
    }
}
