package com.tizo.delivery.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderItemExtra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "extra_value")
    private BigDecimal price;

    @Column(name = "extra_limit")
    private Long limit;

    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    @JsonIgnore
    private OrderItem orderItem;

    public OrderItemExtra() {
    }

    public OrderItemExtra(String name, BigDecimal value, Long limit, Long quantity, OrderItem orderItem) {
        this.name = name;
        this.price = value;
        this.limit = limit;
        this.quantity = quantity;
        this.orderItem = orderItem;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
