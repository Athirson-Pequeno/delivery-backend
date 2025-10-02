package com.tizo.delivery.model.order;

import jakarta.persistence.Embeddable;

@Embeddable
public class CustomerAddress {
    private String street;
    private String number;
    private String neighborhood;

    public CustomerAddress() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

}
