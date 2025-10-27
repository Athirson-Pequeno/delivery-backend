package com.tizo.delivery.model.dto.auth;

import com.tizo.delivery.model.delivery.Delivery;
import com.tizo.delivery.model.store.Address;
import com.tizo.delivery.model.store.Store;

import java.util.Set;

public record StoreSigingResponseDto(String id, String name, String slug, String phoneNumber, Address address,
                                     Set<Delivery> delivery, AuthResponseDto auth) {

    public StoreSigingResponseDto(Store store, AuthResponseDto auth) {
        this(store.getId(), store.getName(), store.getSlug(), store.getPhoneNumber(), store.getAddress(), store.getDeliveries(), auth);
    }
}
