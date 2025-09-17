package com.tizo.delivery.model.dto.order;

import java.util.Set;

public record OrderItemRequestDto(Long productId, Integer quantity, Set<ProductOrdersExtrasDto> extras) {
}
