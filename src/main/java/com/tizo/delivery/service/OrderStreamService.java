package com.tizo.delivery.service;

import com.tizo.delivery.model.dto.order.OrderResponseDto;
import com.tizo.delivery.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderStreamService {

    private static final Logger log = LoggerFactory.getLogger(OrderStreamService.class);

    private final OrderRepository orderRepository;

    public OrderStreamService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    private final Map<String, Map<String, SseEmitter>> emittersByStore = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String storeId, String clientId) {
        emittersByStore.computeIfAbsent(storeId, k -> new ConcurrentHashMap<>());

        SseEmitter emitter = new SseEmitter(600_000L);
        emittersByStore.get(storeId).put(clientId, emitter);

        emitter.onCompletion(() -> removeEmitter(storeId, clientId));
        emitter.onTimeout(() -> removeEmitter(storeId, clientId));

        try {
            emitter.send(SseEmitter.event()
                    .name("init")
                    .data("Conexão aberta para loja " + storeId));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }

    private void removeEmitter(String storeId, String clientId) {
        Map<String, SseEmitter> storeEmitters = emittersByStore.get(storeId);
        if (storeEmitters != null) {
            storeEmitters.remove(clientId);
            if (storeEmitters.isEmpty()) {
                emittersByStore.remove(storeId);
            }
        }
    }

    public void emitOrderCreated(String storeId, OrderResponseDto order) {
        broadcastToStore(storeId, "order-created", order);
    }

    public void emitPaymentUpdate(String storeId, String paymentId, String status) {
        broadcastToStore(storeId, "payment-update", Map.of("paymentId", paymentId, "status", status));
    }

    private void broadcastToStore(String storeId, String eventName, Object data) {
        Map<String, SseEmitter> storeEmitters = emittersByStore.get(storeId);
        if (storeEmitters == null) return;

        storeEmitters.forEach((clientId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            } catch (IOException e) {
                log.warn("Erro ao enviar evento SSE para loja {} cliente {}: {}", storeId, clientId, e.getMessage());
                emitter.completeWithError(e);
                removeEmitter(storeId, clientId);
            }
        });
    }
}
