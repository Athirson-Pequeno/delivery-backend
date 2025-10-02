package com.tizo.delivery.model.dto.order;

import com.tizo.delivery.model.order.OrderItem;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

public record OrderItemResponse(
        Long id,
        Long productId,
        String productName,
        String size,
        String sizeDescription,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal subtotal,
        BigDecimal extrasTotal,
        BigDecimal total,
        Set<OrderItemExtraGroupResponse> extrasGroup
) {
    public static OrderItemResponse fromEntity(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getSize(),
                item.getSizeDescription(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getSubtotal(),
                item.getExtrasTotal(),
                item.getTotal(),
                item.getExtrasGroup().stream()
                        .map(OrderItemExtraGroupResponse::fromEntity)
                        .collect(Collectors.toSet())
        );
    }
}
