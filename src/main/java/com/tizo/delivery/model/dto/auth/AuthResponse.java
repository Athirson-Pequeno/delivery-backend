package com.tizo.delivery.model.dto.auth;

public record AuthResponse(String accessToken, String refreshToken, String storeId, String storeSlug) {
}
