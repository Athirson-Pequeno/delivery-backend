package com.tizo.delivery.model.dto.order;

import com.tizo.delivery.model.ProductSize;

import java.util.List;

public record OrderItemRequestDto(Long productId, Integer quantity, List<ProductOrdersExtrasDto> extras, ProductSize productSize) {
}
