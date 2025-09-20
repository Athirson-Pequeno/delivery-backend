package com.tizo.delivery.model.dto.product;

import com.tizo.delivery.model.Product;
import com.tizo.delivery.model.ProductSize;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

public record ProductDto(Long id, String name, String description, BigDecimal price, String imagePath, String category,
                         List<ProductSize> productSizes, List<ProductExtrasGroupDto> extrasGroups) {
    public ProductDto(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                ServletUriComponentsBuilder.fromCurrentContextPath().toUriString().concat("/") + product.getImagePath(),
                product.getCategory(),
                product.getProductSize(),
                product.getExtrasGroups().stream().map(ProductExtrasGroupDto::new).toList());
    }
}