package com.tizo.delivery.model.dto.order;

import com.tizo.delivery.model.product.ProductSize;

import java.util.List;

public record OrderItemRequestDto(
        Long productId,
        Integer quantity,
        ProductSize productSize,
        List<ProductOrderExtrasGroupsDto> extraGroup) {
}
