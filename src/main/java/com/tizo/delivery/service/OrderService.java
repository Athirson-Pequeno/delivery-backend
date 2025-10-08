package com.tizo.delivery.service;

import com.tizo.delivery.exception.exceptions.ResourceOwnershipException;
import com.tizo.delivery.model.dto.PageResponseDto;
import com.tizo.delivery.model.dto.order.OrderRequestDto;
import com.tizo.delivery.model.dto.order.OrderResponseDto;
import com.tizo.delivery.model.enums.OrderStatus;
import com.tizo.delivery.model.enums.PaymentStatus;
import com.tizo.delivery.model.order.Order;
import com.tizo.delivery.model.order.OrderItem;
import com.tizo.delivery.model.order.Payment;
import com.tizo.delivery.model.store.Store;
import com.tizo.delivery.repository.OrderRepository;
import com.tizo.delivery.repository.ProductRepository;
import com.tizo.delivery.repository.StoreRepository;
import com.tizo.delivery.util.OrderItemFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final OrderStreamService orderStreamService;

    public OrderService(OrderRepository orderRepository,
                        StoreRepository storeRepository,
                        ProductRepository productRepository,
                        OrderStreamService orderStreamService) {
        this.orderRepository = orderRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.orderStreamService = orderStreamService;
    }

    @Transactional
    public OrderResponseDto createOrder(String storeId, String orderId, OrderRequestDto orderRequestDto) {

        //Recupera a loja pelo ID ou lança exceção se não existir -- mudar para o exception handler
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        //Remove pedido existente com mesmo ID, se houver, garantindo atualização
        if (orderRepository.existsOrderById(orderId)) {
            orderRepository.deleteOrderById(orderId);
            orderRepository.flush();
        }

        //Cria novo pedido
        Order order = new Order();

        //Popula dados básicos do pedido
        order.setId(orderId);
        order.setStore(store);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setCustomerInfos(orderRequestDto.customerInfo());
        order.setObservation(orderRequestDto.observation());

        //Cria itens do pedido a partir do DTO
        List<OrderItem> orderItems = orderRequestDto.items().stream()
                .map(dto -> OrderItemFactory.create(productRepository.findById(dto.productId()).orElseThrow(), dto, order))
                .collect(Collectors.toList());

        order.setItems(orderItems);

        // Inicializa pagamento
        order.setPayment(new Payment());

        // Calcula valores do pagamento
        BigDecimal totalValue = orderItems.stream()
                .map(OrderItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.getPayment().setTotalAmount(totalValue);
        order.getPayment().setMethod(orderRequestDto.payment().getMethod());
        order.getPayment().setPaymentStatus(PaymentStatus.PENDING);
        order.getPayment().setFee(orderRequestDto.payment().getFee());
        order.getPayment().setDiscount(BigDecimal.ZERO);
        order.getPayment().setFinalAmount(totalValue.subtract(order.getPayment().getDiscount())
                .add(order.getPayment().getFee()));
        order.getPayment().setChange(orderRequestDto.payment().getChange());

        // Salva o pedido e retorna o DTO de resposta
        Order createdOrder = orderRepository.save(order);
        OrderResponseDto orderResponseDto = new OrderResponseDto(createdOrder);

        // Emite Pedido criado
        orderStreamService.emitOrderCreated(storeId, orderResponseDto);

        return orderResponseDto;
    }

    public PageResponseDto<OrderResponseDto> getOrdersByStoreId(String storeId, Integer page, Integer size) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Loja não encontrada, id: " + storeId));

        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAllByStoreId(store.getId(), pageable);

        List<OrderResponseDto> dtos = orderPage.stream()
                .map(OrderResponseDto::new)
                .toList();

        return new PageResponseDto<>(dtos, orderPage);
    }

    public OrderResponseDto getOrderById(String storeId, String orderID) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Loja não encontrada, id: " + storeId));

        Order order = orderRepository.findByIdAndStore_Id(orderID, store.getId());

        if (order == null) {
            throw new EntityNotFoundException("Pedido não encontrado, pedido ID: " + orderID + " na loja: " + storeId);
        }

        if (!order.getStore().getId().equals(store.getId())) {
            throw new ResourceOwnershipException("Pedido não pertence a loja, store id; " + storeId);
        }

        return new OrderResponseDto(order);
    }

    public Page<OrderResponseDto> findByStoreAndDate(String storeId, LocalDate date, Pageable pageable) {
        LocalDateTime start = date.minusDays(100).atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        System.out.println(start);
        System.out.println(end);

        return orderRepository.findAllByStoreIdAndCreatedAtBetween(storeId, start, end, pageable)
                .map(OrderResponseDto::fromEntity);
    }
}
