package com.tizo.delivery.repository;

import com.tizo.delivery.model.product.ProductExtras;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtrasRepository extends JpaRepository<ProductExtras, Long> {
}
