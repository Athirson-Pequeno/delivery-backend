package com.tizo.delivery.repository;

import com.tizo.delivery.model.RefreshToken;
import com.tizo.delivery.model.StoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(StoreUser user);
}
