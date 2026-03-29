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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "CRUD de produtos — BC Core")
public class ProductController {

    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar produto")
    public ProductResponse create(@Valid @RequestBody CreateProductRequest request) {
        Product product = productUseCase.createProduct(
                new ProductName(request.name()),
                new Money(request.price()),
                CategoryId.of(request.categoryId())
        );
        return ProductResponse.from(product);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ProductResponse findById(@PathVariable UUID id) {
        return ProductResponse.from(productUseCase.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar produtos")
    public Page<ProductResponse> list(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String status,
            Pageable pageable) {

        CategoryId catId = categoryId != null ? CategoryId.of(categoryId) : null;
        ProductStatus productStatus = status != null ? ProductStatus.valueOf(status) : null;

        Page<Product> page = productUseCase.listProducts(catId, productStatus, pageable);
        return new PageImpl<>(
                page.getContent().stream().map(ProductResponse::from).toList(),
                pageable,
                page.getTotalElements()
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    public ProductResponse update(@PathVariable UUID id,
                                  @Valid @RequestBody UpdateProductRequest request) {
        Product product = productUseCase.updateProduct(
                id,
                new ProductName(request.name()),
                new Money(request.price()),
                CategoryId.of(request.categoryId())
        );
        return ProductResponse.from(product);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Desativar produto")
    public ProductResponse deactivate(@PathVariable UUID id) {
        return ProductResponse.from(productUseCase.deactivateProduct(id));
    }
}
