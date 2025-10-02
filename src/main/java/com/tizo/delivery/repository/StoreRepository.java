package com.tizo.delivery.repository;

import com.tizo.delivery.model.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, String> {
    long countStoreBySlug(String slug);

    Optional<Store> getStoreBySlug(String slug);
}
