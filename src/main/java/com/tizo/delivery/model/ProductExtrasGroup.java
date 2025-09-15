package com.tizo.delivery.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products_extras_groups")
public class ProductExtrasGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long minSelections;
    private Long maxSelections;

    @OneToMany(mappedBy = "extraGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductExtras> extras = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductExtrasGroup() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMinSelections() {
        return minSelections;
    }

    public void setMinSelections(Long minSelections) {
        this.minSelections = minSelections;
    }

    public Long getMaxSelections() {
        return maxSelections;
    }

    public void setMaxSelections(Long maxSelections) {
        this.maxSelections = maxSelections;
    }

    public List<ProductExtras> getExtras() {
        return extras;
    }

    public void setExtras(List<ProductExtras> extras) {
        this.extras = extras;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductExtrasGroup that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}