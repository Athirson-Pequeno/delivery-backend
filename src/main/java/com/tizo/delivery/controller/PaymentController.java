package com.tizo.delivery.controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.tizo.delivery.model.dto.order.OrderItemRequestDto;
import com.tizo.delivery.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("{storeId}/pix")
    public ResponseEntity<?> createPixPayment(@PathVariable String storeId, @RequestBody List<OrderItemRequestDto> body) throws MPException, MPApiException {
        return ResponseEntity.ok(paymentService.createPixPayment(storeId, body));
    }

    @GetMapping("/pix/{preferenceId}")
    public ResponseEntity<?> getPixInfo(@PathVariable String preferenceId) {
        return paymentService.getPixInfo(preferenceId);
    }
}
