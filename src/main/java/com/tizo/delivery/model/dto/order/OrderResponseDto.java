package com.tizo.delivery.model.dto.order;

import com.tizo.delivery.model.order.CustomerInfo;
import com.tizo.delivery.model.order.Order;
import com.tizo.delivery.model.order.OrderItem;
import com.tizo.delivery.model.order.Payment;
import com.tizo.delivery.model.enums.OrderStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderResponseDto(
        String id,
        String storeName,
        OrderStatus orderStatus,
        Instant createdAt,
        Instant updatedAt,
        String observation,
        Payment payment,
        CustomerInfo customerInfo,
        List<OrderItemResponse> orderItems
) {

    public OrderResponseDto(Order order) {
        this(
                order.getId(),
                order.getStore().getName(),
                order.getOrderStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getObservation(),
                order.getPayment(),
                order.getCustomerInfos(),
                order.getItems().stream().map(OrderItemResponse::fromEntity).collect(Collectors.toList())
        );
    }

    public static OrderResponseDto fromEntity(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getStore().getName(),
                order.getOrderStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getObservation(),
                order.getPayment(),
                order.getCustomerInfos(),
                order.getItems().stream().map(OrderItemResponse::fromEntity).collect(Collectors.toList())
        );
    }
}
