package com.example.hexagonal.infrastructure.adapter.in.rest;

import com.example.hexagonal.domain.model.Product;
import com.example.hexagonal.domain.model.ProductStatus;
import com.example.hexagonal.domain.port.in.ProductUseCase;
import com.example.hexagonal.domain.valueobject.CategoryId;
import com.example.hexagonal.domain.valueobject.Money;
import com.example.hexagonal.domain.valueobject.ProductName;
import com.example.hexagonal.infrastructure.adapter.in.rest.dto.CreateProductRequest;
import com.example.hexagonal.infrastructure.adapter.in.rest.dto.ProductResponse;
import com.example.hexagonal.infrastructure.adapter.in.rest.dto.UpdateProductRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
class ProductController {

    private final ProductUseCase productUseCase;

    ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        Product product = productUseCase.createProduct(
                new ProductName(request.name()),
                new Money(request.price()),
                new CategoryId(request.categoryId())
        );
        return ProductResponse.from(product);
    }

    @GetMapping("/{id}")
    ProductResponse findById(@PathVariable UUID id) {
        return ProductResponse.from(productUseCase.findById(id));
    }

    @GetMapping
    Page<ProductResponse> list(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) ProductStatus status,
            Pageable pageable) {
        CategoryId catId = categoryId != null ? new CategoryId(categoryId) : null;
        return productUseCase.listProducts(catId, status, pageable).map(ProductResponse::from);
    }

    @PutMapping("/{id}")
    ProductResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateProductRequest request) {
        Product product = productUseCase.updateProduct(
                id,
                new ProductName(request.name()),
                new Money(request.price()),
                new CategoryId(request.categoryId())
        );
        return ProductResponse.from(product);
    }

    @PatchMapping("/{id}/deactivate")
    ProductResponse deactivate(@PathVariable UUID id) {
        return ProductResponse.from(productUseCase.deactivateProduct(id));
    }
}
