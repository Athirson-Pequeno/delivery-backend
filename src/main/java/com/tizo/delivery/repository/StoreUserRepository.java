package com.tizo.delivery.repository;

import com.tizo.delivery.model.StoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreUserRepository extends JpaRepository<StoreUser, String> {
    Optional<StoreUser> findByEmail(String email);
}
