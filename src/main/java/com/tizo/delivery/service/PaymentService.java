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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@Service
public class PaymentService {

    @Value("${api.token}")
    private String accessToken;

    private ProductService productService;

    public PaymentService(ProductService productService) {
        this.productService = productService;
    }

    /*public ResponseEntity<Map> createPixPayment(Map<String, Object> body) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> preference = new HashMap<>();
        preference.put("items", body.get("items"));
        preference.put("payer", body.get("payer"));

        Map<String, Object> paymentMethods = new HashMap<>();
        //paymentMethods.put("excluded_payment_types", new Map[]{ Map.of("id", "credit_card") });
        paymentMethods.put("installments", 1);
        preference.put("payment_methods", paymentMethods);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(preference, headers);


        Map response = restTemplate.postForObject(
                "https://api.mercadopago.com/checkout/preferences",
                entity,
                Map.class
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> generatePixQrCode(Map<String, Object> body) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> preferencePayments = new HashMap<>();
        preferencePayments.put("transaction_amount", body.get("transaction_amount"));
        preferencePayments.put("payment_method_id", body.get("payment_method_id"));
        preferencePayments.put("description", body.get("description"));

        Map<String, Object> payerInfos = (Map<String, Object>) body.get("payer");
        payerInfos.put("email", payerInfos.get("email"));

        preferencePayments.put("payer", payerInfos);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.set("X-Idempotency-Key", UUID.randomUUID().toString());

        System.out.println(preferencePayments);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(preferencePayments, headers);

        Map response = restTemplate.postForObject(
                "https://api.mercadopago.com/v1/payments",
                entity,
                Map.class
        );

        System.out.println(response);

        return ResponseEntity.ok(response);
    }*/

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
                .transactionAmount(transactionAmount)
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
}
