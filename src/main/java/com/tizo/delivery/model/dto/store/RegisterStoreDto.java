package com.tizo.delivery.model.dto.store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterStoreDto(
        @NotBlank
        @Size(max = 100)
        String name,

        @NotBlank
        @Size(max = 100)
        String email,

        @NotBlank
        @Size(min = 6, max = 100)
        String password,

        @NotBlank
        @Size(max = 255)
        String address,

        @NotBlank
        @Size(max = 15)
        String phoneNumber) {
}
