package com.tizo.delivery.model.dto.order;

import com.tizo.delivery.model.OrderItem;
import com.tizo.delivery.model.dto.order.OrderItemExtraResponse;

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
        Set<OrderItemExtraResponse> extras
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
                item.getExtras().stream()
                        .map(OrderItemExtraResponse::fromEntity)
                        .collect(Collectors.toSet())
        );
    }
}
