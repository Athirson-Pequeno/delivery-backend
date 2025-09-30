package com.tizo.delivery.model.dto.product;

import com.tizo.delivery.model.ProductExtras;

import java.math.BigDecimal;

public record ProductExtrasDto(
        Long id,
        String name,
        BigDecimal value,
        Long limit,
        Boolean active
) {
    public ProductExtrasDto(ProductExtras extra) {
        this(
                extra.getId(),
                extra.getName(),
                extra.getValue(),
                extra.getLimit(),
                extra.getActive()
        );
    }
}