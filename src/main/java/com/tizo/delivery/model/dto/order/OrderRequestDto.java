package com.tizo.delivery.model.dto.order;

import com.tizo.delivery.model.order.CustomerInfo;
import com.tizo.delivery.model.order.Payment;

import java.util.List;

public record OrderRequestDto(
        CustomerInfo customerInfo,
        String observation,
        Payment payment,
        List<OrderItemRequestDto> items
) {
}
