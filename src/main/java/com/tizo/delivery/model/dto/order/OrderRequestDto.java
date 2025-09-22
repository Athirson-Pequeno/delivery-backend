package com.tizo.delivery.model.dto.order;

import com.tizo.delivery.model.CustomerInfo;
import com.tizo.delivery.model.Payment;

import java.util.List;

public record OrderRequestDto(
        CustomerInfo customerInfo,
        Payment payment,
        List<OrderItemRequestDto> items
) {
}
