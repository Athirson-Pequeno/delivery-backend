package com.tizo.delivery.model.dto.auth;

public record AuthResponseDto(String accessToken, String refreshToken, String storeId, String storeSlug) {
}
