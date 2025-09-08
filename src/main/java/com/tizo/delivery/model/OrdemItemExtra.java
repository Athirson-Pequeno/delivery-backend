package com.tizo.delivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.math.BigDecimal;

@Embeddable
public class OrdemItemExtra {


    private String name;

    @Column(name = "extra_value")
    private BigDecimal value;

    @Column(name = "extra_limit")
    private Long limit;

    private Long quantity;

    public OrdemItemExtra() {
    }

    public OrdemItemExtra(String name, BigDecimal value, Long limit, Long quantity) {
        this.name = name;
        this.value = value;
        this.limit = limit;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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

}
