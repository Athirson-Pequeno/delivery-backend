package com.tizo.delivery.model.dto;

import com.tizo.delivery.model.Product;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public record ProductDto(Long id, String name, String description, Double price, String imagePath, String category) {
    public ProductDto(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                ServletUriComponentsBuilder.fromCurrentContextPath().toUriString().concat("/") + product.getImagePath(),
                product.getCategory());
    }
}