package com.tizo.delivery.model.dto.order;

import java.util.List;

public record ProductOrderExtrasGroupsDto(Long extraGroupId, List<ProductOrdersExtrasDto> extras) {
}
