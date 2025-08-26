package com.tizo.delivery.repository;

import com.tizo.delivery.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getProductsByStoreId(String storeId);
    Page<Product> findByStoreId(String id, Pageable pageable);
}
