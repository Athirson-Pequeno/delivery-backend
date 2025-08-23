package com.tizo.delivery.repository;

import com.tizo.delivery.model.CustomerInfos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerInfosRepository extends JpaRepository<CustomerInfos, Long> {
}
