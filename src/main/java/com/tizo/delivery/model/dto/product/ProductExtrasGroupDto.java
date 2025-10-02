package com.tizo.delivery.model.dto.product;


import com.tizo.delivery.model.product.ProductExtrasGroup;

import java.util.List;

public record ProductExtrasGroupDto(
        Long id,
        String name,
        Long minSelections,
        Long maxSelections,
        List<ProductExtrasDto> extras
) {
    public ProductExtrasGroupDto(ProductExtrasGroup group) {
        this(
                group.getId(),
                group.getName(),
                group.getMinSelections(),
                group.getMaxSelections(),
                group.getExtras().stream().map(ProductExtrasDto::new).toList()
        );
    }
}
