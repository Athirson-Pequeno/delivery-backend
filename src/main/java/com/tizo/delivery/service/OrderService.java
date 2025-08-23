package com.tizo.delivery.service;

import com.tizo.delivery.model.*;
import com.tizo.delivery.model.dto.OrderItemRequestDto;
import com.tizo.delivery.model.dto.OrderResponseDto;
import com.tizo.delivery.model.dto.PageResponse;
import com.tizo.delivery.model.enums.OrderStatus;
import com.tizo.delivery.repository.OrderRepository;
import com.tizo.delivery.repository.ProductRepository;
import com.tizo.delivery.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    public OrderResponseDto createOrder(String storeId, List<OrderItemRequestDto> items) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));


        Order order = new Order();
        order.setStore(store);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        List<OrderItem> orderItems;

        orderItems = items.stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemDto.productId()));
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(itemDto.productId());
            orderItem.setQuantity(itemDto.quantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(product.getPrice() * itemDto.quantity());
            orderItem.setOrder(order);
            return orderItem;
        }).toList();

        order.setItems(orderItems);

        order.setPayment(order.getPayment() == null ? new Payment() : order.getPayment());
        order.setDelivery(order.getDelivery() == null ? new Delivery() : order.getDelivery());
        order.setCustomerInfos(order.getCustomerInfos() == null ? new CustomerInfos() : order.getCustomerInfos());

        BigDecimal totalValue = BigDecimal.valueOf(orderItems.stream().mapToDouble(OrderItem::getTotalPrice).sum());
        order.getPayment().setFinalAmount(totalValue);

        return new OrderResponseDto(orderRepository.save(order));
    }

    public PageResponse<OrderResponseDto> getOrdersByStoreId(String storeId, Integer page, Integer size) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAllByStoreId(store.getId(), pageable);

        List<OrderResponseDto> dtos = orderPage.stream()
                .map(OrderResponseDto::new)
                .toList();

        return new PageResponse<>(dtos, orderPage);
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
