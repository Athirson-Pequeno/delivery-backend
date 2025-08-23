package com.tizo.delivery.repository;

import com.tizo.delivery.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByStoreId(String storeId, Pageable pageable);

    Order findByIdAndStore_Id(String id, String storeId);
}
