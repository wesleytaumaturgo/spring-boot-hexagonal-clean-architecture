package com.example.hexagonal.infrastructure.config;

import com.example.hexagonal.application.ProductService;
import com.example.hexagonal.domain.port.in.ProductUseCase;
import com.example.hexagonal.domain.port.out.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ProductUseCase productUseCase(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}
