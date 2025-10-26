package com.tizo.delivery.model.dto.store;

import com.tizo.delivery.model.delivery.Delivery;
import com.tizo.delivery.model.dto.PageResponseDto;
import com.tizo.delivery.model.dto.product.ProductDto;
import com.tizo.delivery.model.product.Product;
import com.tizo.delivery.model.store.Address;
import com.tizo.delivery.model.store.Store;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record StoreProductsDto(String id, String storeSlug, String name, Address address, String phoneNumber,
                               Set<Delivery> delivery,
                               List<String> productCategories,
                               List<String> allCategories,
                               PageResponseDto<ProductDto> products) {
    public StoreProductsDto(Store store, Page<ProductDto> products) {
        this(
                store.getId(),
                store.getSlug(),
                store.getName(),
                store.getAddress(),
                store.getPhoneNumber(),
                store.getDeliveries(),
                products.stream().map(ProductDto::category).sorted().distinct().collect(Collectors.toList()),
                store.getProducts().stream().map(Product::getCategory).sorted().distinct().collect(Collectors.toList()),
                new PageResponseDto<>(products)
        );
    }
}
