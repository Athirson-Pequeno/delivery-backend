package com.tizo.delivery.model.dto.store;

import com.tizo.delivery.model.Store;

public record ResponseStoreDto(String id, String name, String address, String phoneNumber) {

    public ResponseStoreDto(Store store) {
        this(store.getId(), store.getName(), store.getAddress(), store.getPhoneNumber());
    }
}
