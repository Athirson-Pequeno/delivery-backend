package com.tizo.delivery.model.dto.order;

import com.tizo.delivery.model.CustomerInfo;
import com.tizo.delivery.model.Delivery;
import com.tizo.delivery.model.Order;
import com.tizo.delivery.model.Payment;
import com.tizo.delivery.model.enums.OrderStatus;
import com.tizo.delivery.model.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderResponseDto(
        String id,
        String storeName,
        OrderStatus orderStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Payment payment,
        CustomerInfo customerInfo,
        List<OrderItemResponse> orderItems) {

    public OrderResponseDto(Order order) {
        this(
                order.getId(),
                order.getStore().getName(),
                order.getOrderStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getPayment(),
                order.getCustomerInfos(),
                order.getItems().stream().map(OrderItemResponse::fromEntity).collect(Collectors.toList())
        );
    }
}
