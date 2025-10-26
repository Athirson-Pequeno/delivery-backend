package com.tizo.delivery.repository;

import com.tizo.delivery.model.delivery.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    Optional<Set<Delivery>> findByStoreId(String storeID);
}
