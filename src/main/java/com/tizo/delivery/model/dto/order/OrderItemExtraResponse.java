package com.tizo.delivery.model.dto.order;

import com.tizo.delivery.model.OrderItemExtra;

import java.math.BigDecimal;

public record OrderItemExtraResponse(Long id, String name, BigDecimal price, Long limit, Long quantity) {
    public static OrderItemExtraResponse fromEntity(OrderItemExtra orderItemExtra) {
        return new OrderItemExtraResponse(
                orderItemExtra.getId(),
                orderItemExtra.getName(),
                orderItemExtra.getPrice(),
                orderItemExtra.getLimit(),
                orderItemExtra.getQuantity()
        );
    }
}
