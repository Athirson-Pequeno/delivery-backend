package com.tizo.delivery.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private String number;
    private String neighborhood;

    public Address() {
    }

    public Address(String street, String number, String neighborhood) {
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
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
