package com.tizo.delivery.repository;

import com.tizo.delivery.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, String> {
}
