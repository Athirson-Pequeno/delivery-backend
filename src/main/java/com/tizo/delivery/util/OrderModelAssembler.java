package com.tizo.delivery.util;

import com.tizo.delivery.controller.OrderController;
import com.tizo.delivery.controller.StoreController;
import com.tizo.delivery.model.dto.OrderResponseDto;
import com.tizo.delivery.model.dto.PageResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class OrderModelAssembler {
    public EntityModel<OrderResponseDto> toModel(OrderResponseDto dto, String storeId, Integer page, Integer size) {
        return EntityModel.of(dto,
                linkTo(methodOn(OrderController.class).getOrderById(storeId, dto.id())).withSelfRel(),
                linkTo(methodOn(OrderController.class).getOrdersByStoreId(storeId, page, size)).withRel("storeOrders"),
                linkTo(methodOn(StoreController.class).findById(storeId,0,100)).withRel("storeHome")
        );
    }

    public EntityModel<OrderResponseDto> toModel(OrderResponseDto dto, String storeId) {
        return EntityModel.of(dto,
                linkTo(methodOn(OrderController.class).getOrderById(storeId, dto.id())).withSelfRel(),
                linkTo(methodOn(StoreController.class).findById(storeId,0,100)).withRel("storeHome")
        );
    }

    public PagedModel<EntityModel<OrderResponseDto>> toPagedModel(PageResponse<OrderResponseDto> pageResponse, String storeId, Integer page, Integer size) {
        List<EntityModel<OrderResponseDto>> content = pageResponse.content().stream()
                .map(dto -> toModel(dto, storeId, page, size))
                .collect(Collectors.toList());

        return PagedModel.of(content, new PagedModel.PageMetadata(
                pageResponse.size(),
                pageResponse.page(),
                pageResponse.totalElements()
        ));
    }
}
