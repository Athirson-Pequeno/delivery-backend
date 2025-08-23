package com.tizo.delivery.model.dto;

import com.tizo.delivery.model.Store;

public record ResponseStoreDto(String id, String email, String name, String address, String phoneNumber) {

    public ResponseStoreDto(Store store) {
        this(store.getId(), store.getEmail(), store.getName(), store.getAddress(), store.getPhoneNumber());
    }
}
