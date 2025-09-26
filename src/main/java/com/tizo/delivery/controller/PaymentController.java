package com.tizo.delivery.controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.tizo.delivery.model.dto.order.OrderItemRequestDto;
import com.tizo.delivery.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pix/{storeId}/{orderId}")
    public ResponseEntity<?> createPixPayment(@PathVariable String storeId, @PathVariable String orderId, @RequestBody List<OrderItemRequestDto> body) throws MPException, MPApiException {
        return new ResponseEntity<>(paymentService.createPixPayment(storeId, body), HttpStatus.OK);
    }

    @GetMapping("/pix/{preferenceId}")
    public ResponseEntity<?> getPixInfo(@PathVariable String preferenceId) {
        return new ResponseEntity<>(paymentService.getPixInfo(preferenceId), HttpStatus.OK);
    }

    @GetMapping(value = "/pix/status/{paymentId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamPaymentStatus(@PathVariable String paymentId) {
        return paymentService.streamPaymentStatus(paymentId);
    }

    @PostMapping(value = "/pix/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> payload) throws MPException, MPApiException {
        paymentService.handleWebhook(payload);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
