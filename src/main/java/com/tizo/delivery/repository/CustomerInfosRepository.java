package com.tizo.delivery.repository;

import com.tizo.delivery.model.order.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerInfosRepository extends JpaRepository<CustomerInfo, Long> {
}
