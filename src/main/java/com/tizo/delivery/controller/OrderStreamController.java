package com.tizo.delivery.controller;

import com.tizo.delivery.service.OrderStreamService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/orders/stream")
public class OrderStreamController {

    private final OrderStreamService orderStreamService;

    public OrderStreamController(OrderStreamService orderStreamService) {
        this.orderStreamService = orderStreamService;
    }

    @GetMapping(value = "/{storeId}/{clientId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable String storeId, @PathVariable String clientId) {
        return orderStreamService.subscribe(storeId, clientId);
    }
}
