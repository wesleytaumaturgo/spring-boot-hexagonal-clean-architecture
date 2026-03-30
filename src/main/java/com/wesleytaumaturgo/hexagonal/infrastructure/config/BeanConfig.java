package com.wesleytaumaturgo.hexagonal.infrastructure.config;

import com.wesleytaumaturgo.hexagonal.application.ProductService;
import com.wesleytaumaturgo.hexagonal.domain.port.in.ProductUseCase;
import com.wesleytaumaturgo.hexagonal.domain.port.out.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ProductUseCase productUseCase(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }
}
