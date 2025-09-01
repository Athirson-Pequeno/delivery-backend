package com.tizo.delivery.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.tizo.delivery.model.dto.order.OrderItemRequestDto;
import com.tizo.delivery.model.dto.product.ProductDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    @Value("${api.token}")
    private String accessToken;

    private final ProductService productService;
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();


    public PaymentService(ProductService productService) {
        this.productService = productService;
    }

    public ResponseEntity<?> getPixInfo(String preferenceId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.mercadopago.com/checkout/preferences/" + preferenceId,
                HttpMethod.GET,
                entity,
                Map.class
        );
        return ok(response.getBody());
    }

    public ResponseEntity<Payment> createPixPayment(String storeId, List<OrderItemRequestDto> orderItems) throws MPException, MPApiException {

        MercadoPagoConfig.setAccessToken(accessToken);

        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put("x-idempotency-key", UUID.randomUUID().toString());

        MPRequestOptions requestOptions = MPRequestOptions.builder()
                .customHeaders(customHeaders)
                .build();

        PaymentClient client = new PaymentClient();

        BigDecimal transactionAmount = orderItems.stream()
                .map(orderItem -> {
                    ProductDto product = productService.getProductById(storeId, orderItem.productId());
                    return BigDecimal.valueOf(product.price())
                            .multiply(BigDecimal.valueOf(orderItem.quantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PaymentCreateRequest createRequest = PaymentCreateRequest.builder()
                .transactionAmount(BigDecimal.valueOf(0.01))
                .description("Pedido #" + new Random().nextInt(100))
                .paymentMethodId("pix")
                .payer(PaymentPayerRequest.builder()
                        .email("teste@gmail.com")
                        .firstName("teste")
                        .build())
                .dateOfExpiration(OffsetDateTime.now().plusMinutes(15))
                .build();
        try {
            return ResponseEntity.ok(client.create(createRequest, requestOptions));
        } catch (MPApiException e) {
            System.out.println("Status: " + e.getApiResponse().getStatusCode());
            System.out.println("Content: " + e.getApiResponse().getContent());
            throw e;
        }
    }

    public SseEmitter streamPaymentStatus(@PathVariable String paymentId) {
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

    public void handleWebhook(Map<String, Object> payload) {
        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        String paymentId = String.valueOf(data.get("id"));

        MercadoPagoConfig.setAccessToken(accessToken);
        PaymentClient client = new PaymentClient();

        try {
            Payment response = client.get(Long.parseLong(paymentId));
            SseEmitter emitter = emitters.get(paymentId);
            if (emitter != null) {
                try {
                    response.getStatus();
                    emitter.send(SseEmitter.event()
                            .name("payment-update")
                            .data(response.getStatus()));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }
        } catch (Exception e) {
            log.error("Erro: {}", e.getMessage());
        }
    }
}
