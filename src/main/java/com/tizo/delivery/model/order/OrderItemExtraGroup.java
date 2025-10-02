package com.tizo.delivery.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class OrderItemExtraGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    @JsonIgnore
    private OrderItem orderItem;

    @OneToMany(mappedBy = "orderItemExtraGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItemExtra> extras = new HashSet<>();

    public OrderItemExtraGroup() {
    }

    public OrderItemExtraGroup(String name, OrderItem orderItem, Set<OrderItemExtra> extras) {
        this.name = name;
        this.orderItem = orderItem;
        this.extras = extras;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Set<OrderItemExtra> getExtras() {
        return extras;
    }

    public void setExtras(Set<OrderItemExtra> extras) {
        this.extras = extras;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
