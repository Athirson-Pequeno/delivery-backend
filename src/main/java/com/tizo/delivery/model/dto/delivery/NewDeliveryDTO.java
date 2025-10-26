package com.tizo.delivery.model.dto.delivery;

import java.math.BigDecimal;

public record NewDeliveryDTO(String neighborhood, BigDecimal tax) {
}
