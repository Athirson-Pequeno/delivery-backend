package com.tizo.delivery.controller;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.tizo.delivery.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/payments")
public class WebHookController {

    private final PaymentService paymentService;

    @Value("${api.token}")
    private String accessToken;

    // Mapa para armazenar eventos SSE por pagamento
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public WebHookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @GetMapping(value = "/status/{paymentId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamPaymentStatus(@PathVariable String paymentId) {

        System.out.println(paymentId + "Stream ");
        SseEmitter emitter = new SseEmitter(600_000L);
        emitters.put(paymentId, emitter);

        emitter.onCompletion(() -> emitters.remove(paymentId));
        emitter.onTimeout(() -> emitters.remove(paymentId));

        try {
            emitter.send(SseEmitter.event()
                    .name("init")
                    .data("Conexão aberta para pagamento " + paymentId));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }


    @PostMapping(value = "/pix")
    public ResponseEntity<Void> handleWebhook(@RequestBody Map<String, Object> payload) throws MPException, MPApiException {
        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        String paymentId = String.valueOf(data.get("id"));

        MercadoPagoConfig.setAccessToken(accessToken);
        PaymentClient client = new PaymentClient();
        Payment response = client.get(Long.parseLong(paymentId));

        SseEmitter emitter = emitters.get(paymentId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("payment-update")
                        .data(response.getStatus()));
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        }

        return ResponseEntity.ok().build();
    }
}
