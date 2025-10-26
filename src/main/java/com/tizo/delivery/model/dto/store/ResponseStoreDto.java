package com.tizo.delivery.model.dto.store;

import com.tizo.delivery.model.delivery.Delivery;
import com.tizo.delivery.model.store.Address;
import com.tizo.delivery.model.store.Store;

import java.util.Set;

public record ResponseStoreDto(String id, String name, String slug, String phoneNumber, Address address,
                               Set<Delivery> delivery) {

    public ResponseStoreDto(Store store) {
        this(store.getId(), store.getName(), store.getSlug(), store.getPhoneNumber(), store.getAddress(), store.getDeliveries());
    }
}
