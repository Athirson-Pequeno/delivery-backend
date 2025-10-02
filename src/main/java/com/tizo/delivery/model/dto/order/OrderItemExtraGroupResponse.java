package com.tizo.delivery.model.dto.order;

import com.tizo.delivery.model.order.OrderItemExtraGroup;

import java.util.Set;
import java.util.stream.Collectors;

public record OrderItemExtraGroupResponse(
        String name,
        Set<OrderItemExtraResponse> extras
) {
    public static OrderItemExtraGroupResponse fromEntity(OrderItemExtraGroup group) {
        return new OrderItemExtraGroupResponse(
                group.getName(),
                group.getExtras().stream()
                        .map(OrderItemExtraResponse::fromEntity)
                        .collect(Collectors.toSet())
        );
    }
}
