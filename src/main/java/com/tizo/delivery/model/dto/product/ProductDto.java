package com.tizo.delivery.model.dto.product;

import com.tizo.delivery.model.Product;
import com.tizo.delivery.model.ProductExtras;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Set;

public record ProductDto(Long id, String name, String description, Double price, String imagePath, String category,
                         Set<ProductExtras> productExtras) {
    public ProductDto(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                ServletUriComponentsBuilder.fromCurrentContextPath().toUriString().concat("/") + product.getImagePath(),
                product.getCategory(),
                product.getExtras());
    }
}