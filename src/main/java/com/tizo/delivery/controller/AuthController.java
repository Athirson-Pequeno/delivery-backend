package com.tizo.delivery.controller;

import com.tizo.delivery.model.dto.auth.AuthCredentials;
import com.tizo.delivery.model.dto.auth.AuthResponse;
import com.tizo.delivery.model.dto.auth.RefreshRequest;
import com.tizo.delivery.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register/manager/{storeId}")
    public ResponseEntity<AuthResponse> registerManager(
            @PathVariable String storeId,
            @Valid @RequestBody AuthCredentials request) {

        AuthResponse response = authService.registerManager(request, storeId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/employee/{storeId}")
    public ResponseEntity<AuthResponse> registerEmployee(
            @PathVariable String storeId,
            @Valid @RequestBody AuthCredentials request) {

        AuthResponse response = authService.registerEmployee(request, storeId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthCredentials request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}
