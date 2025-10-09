package com.tizo.delivery.controller;

import com.tizo.delivery.model.dto.PageResponseDto;
import com.tizo.delivery.model.dto.order.OrderRequestDto;
import com.tizo.delivery.model.dto.order.OrderResponseDto;
import com.tizo.delivery.service.OrderService;
import com.tizo.delivery.util.OrderModelAssembler;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderModelAssembler assembler = new OrderModelAssembler();

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{storeID}/{orderID}")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto,
                                                        @PathVariable String storeID,
                                                        @PathVariable String orderID) {
        OrderResponseDto order = orderService.createOrder(storeID, orderID, orderRequestDto);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{storeID}")
    public ResponseEntity<PagedModel<EntityModel<OrderResponseDto>>> getOrdersByStoreId(
            @PathVariable String storeID,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        PageResponseDto<OrderResponseDto> orders = orderService.getOrdersByStoreId(storeID, page, size);
        PagedModel<EntityModel<OrderResponseDto>> model = assembler.toPagedModel(orders, storeID, page, size);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/{storeId}/{orderId}")
    public ResponseEntity<EntityModel<OrderResponseDto>> getOrderById(
            @PathVariable String storeId,
            @PathVariable String orderId) {

        OrderResponseDto orderDto = orderService.getOrderById(storeId, orderId);
        if (orderDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        EntityModel<OrderResponseDto> model = assembler.toModel(orderDto, storeId);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/days/{storeId}")
    public ResponseEntity<PageResponseDto<OrderResponseDto>> getOrdersByDate(
            @PathVariable String storeId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "50") @Max(50) Integer size) {


        Page<OrderResponseDto> p = orderService.findByStoreAndDate(storeId, size, page);
        PageResponseDto<OrderResponseDto> pageResponseDto = new PageResponseDto<>(p);
        return ResponseEntity.ok(pageResponseDto);
    }

}
