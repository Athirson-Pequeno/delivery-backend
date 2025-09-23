package com.tizo.delivery.service;

import com.tizo.delivery.model.*;
import com.tizo.delivery.model.dto.PageResponseDto;
import com.tizo.delivery.model.dto.order.OrderRequestDto;
import com.tizo.delivery.model.dto.order.OrderResponseDto;
import com.tizo.delivery.model.dto.order.ProductOrdersExtrasDto;
import com.tizo.delivery.model.enums.OrderStatus;
import com.tizo.delivery.model.enums.PaymentStatus;
import com.tizo.delivery.repository.OrderRepository;
import com.tizo.delivery.repository.ProductRepository;
import com.tizo.delivery.repository.StoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, StoreRepository storeRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponseDto createOrder(String storeId, String orderId, OrderRequestDto orderRequestDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        if (orderRepository.existsOrderById(orderId)) {
            orderRepository.deleteOrderById(orderId);
            orderRepository.flush();
        }

        Order order = orderRepository.getOrderById(orderId).orElse(new Order());

        order.setId(orderId);
        order.setStore(store);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setCustomerInfos(orderRequestDto.customerInfo());
        order.setObservation(orderRequestDto.observation());

        List<OrderItem> orderItems = orderRequestDto.items().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemDto.productId()));
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemDto.productId());
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setSize(itemDto.productSize().getSize());
            orderItem.setSizeDescription(itemDto.productSize().getSizeName());
            orderItem.setUnitPrice(product.getProductSize().stream()
                    .filter(size -> size.getId().equals(itemDto.productSize().getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Size not found with id: " + itemDto.productSize().getId()))
                    .getPrice());
            orderItem.setProductName(product.getName());
            orderItem.setOrder(order);
            orderItem.setExtras(
                    (product.getExtras() == null ? Set.<ProductExtras>of() : product.getExtras())
                            .stream()
                            // filtra apenas os extras que o cliente escolheu
                            .filter(extra -> itemDto.extras() != null &&
                                    itemDto.extras().stream()
                                            .anyMatch(e -> e.extraId().equals(extra.getId())))
                            // mapeia para OrderItemExtra copiando dados e adicionando a quantidade escolhida
                            .map(extra -> {
                                // pega a quantidade escolhida do DTO
                                Long quantity = itemDto.extras().stream()
                                        .filter(e -> e.extraId().equals(extra.getId()))
                                        .map(ProductOrdersExtrasDto::extraQuantity)
                                        .findFirst()
                                        .orElse(0L); // default 0 caso não encontre
                                return new OrderItemExtra(extra.getName(), extra.getValue(), extra.getLimit(), quantity, orderItem);
                            })
                            .collect(Collectors.toCollection(java.util.HashSet::new))
            );

            return orderItem;
        }).collect(Collectors.toCollection(java.util.ArrayList::new));

        order.setItems(orderItems);

        order.setPayment(new Payment());

        BigDecimal totalValue = orderItems.stream().map(OrderItem::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.getPayment().setTotalAmount(totalValue);
        order.getPayment().setMethod(orderRequestDto.payment().getMethod());
        order.getPayment().setPaymentStatus(PaymentStatus.PENDING);
        order.getPayment().setFee(orderRequestDto.payment().getFee());
        order.getPayment().setDiscount(BigDecimal.ZERO);
        order.getPayment().setFinalAmount(totalValue.subtract(order.getPayment().getDiscount()).add(order.getPayment().getFee()));
        order.getPayment().setChange(orderRequestDto.payment().getChange());

        return new OrderResponseDto(orderRepository.save(order));
    }

    public PageResponseDto<OrderResponseDto> getOrdersByStoreId(String storeId, Integer page, Integer size) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAllByStoreId(store.getId(), pageable);

        List<OrderResponseDto> dtos = orderPage.stream()
                .map(OrderResponseDto::new)
                .toList();

        return new PageResponseDto<>(dtos, orderPage);
    }

    public OrderResponseDto getOrderById(String storeID, String orderID) {
        Store store = storeRepository.findById(storeID)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeID));

        Order order = orderRepository.findByIdAndStore_Id(orderID, store.getId());

        if (order == null) {
            throw new RuntimeException("Order not found with id: " + orderID + " for store: " + storeID);
        }

        if (!order.getStore().getId().equals(store.getId())) {
            throw new RuntimeException("Order does not belong to the store with id: " + storeID);
        }

        return new OrderResponseDto(order);
    }
}
