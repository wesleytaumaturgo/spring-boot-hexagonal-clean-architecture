package com.wesleytaumaturgo.hexagonal.infrastructure.adapter.in.rest;

import com.wesleytaumaturgo.hexagonal.domain.exception.ProductAlreadyExistsException;
import com.wesleytaumaturgo.hexagonal.domain.exception.ProductNotFoundException;
import com.wesleytaumaturgo.hexagonal.domain.model.Product;
import com.wesleytaumaturgo.hexagonal.domain.model.ProductStatus;
import com.wesleytaumaturgo.hexagonal.domain.port.in.ProductUseCase;
import com.wesleytaumaturgo.hexagonal.domain.valueobject.CategoryId;
import com.wesleytaumaturgo.hexagonal.domain.valueobject.Money;
import com.wesleytaumaturgo.hexagonal.domain.valueobject.ProductName;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// REQ-1.EARS-1, REQ-2.EARS-1, REQ-3.EARS-1, REQ-4.EARS-1, REQ-5.EARS-1
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductUseCase productUseCase;

    @Autowired
    ObjectMapper objectMapper;

    private UUID categoryUuid;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        categoryUuid = UUID.randomUUID();
        sampleProduct = Product.create(
                new ProductName("Notebook Dell"),
                new Money(new BigDecimal("4999.99")),
                new CategoryId(categoryUuid)
        );
    }

    @Test
    void shouldReturn201WhenCreatingValidProduct() throws Exception {
        // REQ-1.EARS-1
        given(productUseCase.createProduct(any(), any(), any())).willReturn(sampleProduct);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Notebook Dell",
                                "price", 4999.99,
                                "categoryId", categoryUuid.toString()
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Notebook Dell"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturn422WhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "",
                                "price", 99.99,
                                "categoryId", categoryUuid.toString()
                        ))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturn422WhenPriceIsNegative() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Product",
                                "price", -1.0,
                                "categoryId", categoryUuid.toString()
                        ))))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldReturn200WhenFindingExistingProduct() throws Exception {
        // REQ-2.EARS-1
        given(productUseCase.findById(sampleProduct.getId())).willReturn(sampleProduct);

        mockMvc.perform(get("/api/v1/products/" + sampleProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleProduct.getId().toString()));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        // REQ-2.EARS-2
        UUID id = UUID.randomUUID();
        given(productUseCase.findById(id)).willThrow(new ProductNotFoundException(id));

        mockMvc.perform(get("/api/v1/products/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200WhenListingProducts() throws Exception {
        // REQ-3.EARS-1
        given(productUseCase.listProducts(any(), any(), any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(sampleProduct)));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void shouldReturn200WhenUpdatingProduct() throws Exception {
        // REQ-4.EARS-1
        given(productUseCase.updateProduct(any(), any(), any(), any())).willReturn(sampleProduct);

        mockMvc.perform(put("/api/v1/products/" + sampleProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Notebook Dell XPS",
                                "price", 5999.99,
                                "categoryId", categoryUuid.toString()
                        ))))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn200WhenDeactivatingProduct() throws Exception {
        // REQ-5.EARS-1
        given(productUseCase.deactivateProduct(sampleProduct.getId())).willReturn(sampleProduct);

        mockMvc.perform(patch("/api/v1/products/" + sampleProduct.getId() + "/deactivate"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn409WhenProductAlreadyExists() throws Exception {
        // REQ-1.EARS-6
        given(productUseCase.createProduct(any(), any(), any()))
                .willThrow(new ProductAlreadyExistsException(categoryUuid));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Notebook Dell",
                                "price", 4999.99,
                                "categoryId", categoryUuid.toString()
                        ))))
                .andExpect(status().isConflict());
    }
}
