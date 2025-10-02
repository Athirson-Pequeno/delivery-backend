package com.tizo.delivery.repository;

import com.tizo.delivery.model.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findAllByStoreId(String storeId, Pageable pageable);
    Order findByIdAndStore_Id(String id, String storeId);

    Optional<Order> getOrderById(String id);

    boolean existsOrderById(String id);

    void deleteOrderById(String id);
}
