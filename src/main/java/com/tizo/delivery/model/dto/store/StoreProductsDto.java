package com.tizo.delivery.model.dto.store;

import com.tizo.delivery.model.Address;
import com.tizo.delivery.model.Store;
import com.tizo.delivery.model.dto.PageResponse;
import com.tizo.delivery.model.dto.product.ProductDto;
import org.springframework.data.domain.Page;

import java.util.Set;

public record StoreProductsDto(String id, String storeSlug, String name, Address address, String phoneNumber,
                               Set<String> productCategories,
                               PageResponse<ProductDto> products) {
    public StoreProductsDto(Store store, Page<ProductDto> products) {
        this(
                store.getId(),
                store.getSlug(),
                store.getName(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getCategories(),
                new PageResponse<>(products.getContent(), products)
        );
    }
}
