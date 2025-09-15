package com.tizo.delivery.model.dto.product;

import com.tizo.delivery.model.ProductExtras;

public record ProductExtrasDto(
        Long id,
        String name,
        Double value,
        Long limit,
        Boolean active
) {
    public ProductExtrasDto(ProductExtras extra) {
        this(
                extra.getId(),
                extra.getName(),
                extra.getValue() != null ? extra.getValue().doubleValue() : 0,
                extra.getLimit(),
                extra.getActive()
        );
    }
}