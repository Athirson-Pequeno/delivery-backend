package com.tizo.delivery.model.dto.store;

import com.tizo.delivery.model.Store;
import com.tizo.delivery.model.dto.PageResponse;
import com.tizo.delivery.model.dto.product.ProductDto;
import org.springframework.data.domain.Page;

import java.util.Set;

public record StoreProductsDto(String id, String name, String address, String phoneNumber,
                               Set<String> productCategories,
                               PageResponse<ProductDto> products) {
    public StoreProductsDto(Store store, Page<ProductDto> products) {
        this(
                store.getId(),
                store.getName(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getCategories(),
                new PageResponse<>(products.getContent(), products)
        );
    }
}
