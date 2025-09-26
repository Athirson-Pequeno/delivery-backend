package com.tizo.delivery.controller;

import com.tizo.delivery.model.dto.auth.AuthCredentialsDto;
import com.tizo.delivery.model.dto.auth.AuthResponseDto;
import com.tizo.delivery.model.dto.auth.RefreshRequestDto;
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
    public ResponseEntity<AuthResponseDto> registerManager(
            @PathVariable String storeId,
            @Valid @RequestBody AuthCredentialsDto request) {

        AuthResponseDto response = authService.registerManager(request, storeId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/register/employee/{storeId}")
    public ResponseEntity<AuthResponseDto> registerEmployee(
            @PathVariable String storeId,
            @Valid @RequestBody AuthCredentialsDto request) {

        AuthResponseDto response = authService.registerEmployee(request, storeId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthCredentialsDto request) {
        AuthResponseDto response = authService.authenticate(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestBody RefreshRequestDto request) {
        AuthResponseDto response = authService.refreshToken(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
