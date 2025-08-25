package com.tizo.delivery.controller;

import com.tizo.delivery.model.dto.order.OrderItemRequestDto;
import com.tizo.delivery.model.dto.order.OrderResponseDto;
import com.tizo.delivery.model.dto.PageResponse;
import com.tizo.delivery.service.OrderService;
import com.tizo.delivery.util.OrderModelAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderModelAssembler assembler = new OrderModelAssembler();

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{storeID}")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody List<OrderItemRequestDto> orderItemRequestDtoList, @PathVariable String storeID) {
        OrderResponseDto order = orderService.createOrder(storeID, orderItemRequestDtoList);
        return ResponseEntity.status(201).body(order);
    }

    @GetMapping("/{storeID}")
    public ResponseEntity<PagedModel<EntityModel<OrderResponseDto>>> getOrdersByStoreId(
            @PathVariable String storeID,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        PageResponse<OrderResponseDto> orders = orderService.getOrdersByStoreId(storeID, page, size);
        PagedModel<EntityModel<OrderResponseDto>> model = assembler.toPagedModel(orders, storeID, page, size);
        return ResponseEntity.ok(model);
    }


    @GetMapping("/{storeID}/{orderID}")
    public ResponseEntity<EntityModel<OrderResponseDto>> getOrderById(
            @PathVariable String storeID,
            @PathVariable String orderID) {

        OrderResponseDto orderDto = orderService.getOrderById(storeID, orderID);
        if (orderDto == null) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<OrderResponseDto> model = assembler.toModel(orderDto, storeID);

        return ResponseEntity.ok(model);
    }

}
