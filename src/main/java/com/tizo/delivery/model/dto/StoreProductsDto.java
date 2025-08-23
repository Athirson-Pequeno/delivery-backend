package com.tizo.delivery.model.dto;

import com.tizo.delivery.model.Store;
import org.springframework.data.domain.Page;

import java.util.Set;

public record StoreProductsDto(String id, String email, String name, String address, String phoneNumber,
                               Set<String> productCategories,
                               PageResponse<ProductDto> products) {
    public StoreProductsDto(Store store, Page<ProductDto> products) {
        this(
                store.getId(),
                store.getEmail(),
                store.getName(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getCategories(),
                new PageResponse<>(products.getContent(), products)
        );
    }
}
