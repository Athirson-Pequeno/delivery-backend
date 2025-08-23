package com.tizo.delivery.model.dto;

import com.tizo.delivery.model.*;
import com.tizo.delivery.model.enums.OrderStatus;
import com.tizo.delivery.model.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
        String id,
        String storeName,
        OrderStatus orderStatus,
        BigDecimal totalValue,
        PaymentMethod paymentMethod,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Payment payment,
        Delivery delivery,
        CustomerInfos customerInfos,
        List<OrderItem> orderItems) {

    public OrderResponseDto(Order order) {
        this(
                order.getId(),
                order.getStore().getName(),
                order.getOrderStatus(),
                order.getPayment().getFinalAmount(),
                order.getPayment().getMethod(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getPayment(),
                order.getDelivery(),
                order.getCustomerInfos(),
                order.getItems()
        );
    }
}
