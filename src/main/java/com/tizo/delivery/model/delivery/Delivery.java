package com.tizo.delivery.model.delivery;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tizo.delivery.model.store.Store;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Table(name = "delivery")
@Entity
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String neighborhood;

    private BigDecimal tax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @JsonIgnore()
    private Store store;

    public Delivery() {
    }

    public Delivery(String neighborhood, BigDecimal tax, Store store) {
        this.neighborhood = neighborhood;
        this.tax = tax;
        this.store = store;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}