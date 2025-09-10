package com.tizo.delivery.service;

import com.tizo.delivery.model.RefreshToken;
import com.tizo.delivery.model.Store;
import com.tizo.delivery.model.StoreUser;
import com.tizo.delivery.model.UserDetailsImpl;
import com.tizo.delivery.model.dto.auth.AuthCredentials;
import com.tizo.delivery.model.dto.auth.AuthResponse;
import com.tizo.delivery.model.dto.auth.RefreshRequest;
import com.tizo.delivery.model.enums.StoreUserRole;
import com.tizo.delivery.repository.RefreshTokenRepository;
import com.tizo.delivery.repository.StoreRepository;
import com.tizo.delivery.repository.StoreUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    // Repositórios para acesso ao banco de dados
    private final StoreUserRepository storeUserRepository;          // Usuários da loja
    private final StoreRepository storeRepository;                  // Lojas
    private final JwtService jwtService;                            // Serviço de JWT para gerar tokens
    private final RefreshTokenRepository refreshTokenRepository;    // Refresh tokens
    private final PasswordEncoder passwordEncoder;                  // Para criptografar senhas
    private final AuthenticationManager authenticationManager;      // Para autenticação

    public AuthService(
            StoreRepository storeRepository,
            StoreUserRepository storeUserRepository,
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.storeUserRepository = storeUserRepository;
        this.storeRepository = storeRepository;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // Registro de gerente
    public AuthResponse registerManager(AuthCredentials registerRequest, String storeId) {
        // Delegando para método genérico com role MANAGER
        return registerManager(registerRequest, storeId, StoreUserRole.MANAGER);
    }

    // Registro de funcionário
    public AuthResponse registerEmployee(AuthCredentials registerRequest, String storeId) {
        // Delegando para método genérico com role EMPLOYEE
        return registerManager(registerRequest, storeId, StoreUserRole.EMPLOYEE);
    }

    // Método genérico de registro
    public AuthResponse registerManager(AuthCredentials registerRequest, String storeId, StoreUserRole storeUserRole) {
        // Valida se email já está cadastrado
        if (storeUserRepository.existsByEmail(registerRequest.email())) {
            throw new RuntimeException("Email já cadastrado");
        }

        // Cria novo usuário
        StoreUser storeUser = new StoreUser();
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store with id: " + storeId + " not found"));

        // Configura dados do usuário
        storeUser.setEmail(registerRequest.email());
        storeUser.setPassword(passwordEncoder.encode(registerRequest.password())); // Criptografa senha
        storeUser.setRole(storeUserRole); // Role: MANAGER ou EMPLOYEE
        storeUser.setStore(store);         // Associação à loja
        storeUserRepository.save(storeUser); // Salva no banco

        // Cria tokens JWT
        UserDetailsImpl userDetails = new UserDetailsImpl(storeUser);
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Salva refresh token no banco
        saveRefreshToken(storeUser, refreshToken);

        // Retorna tokens para frontend
        return new AuthResponse(accessToken, refreshToken, storeUser.getStore().getId(), storeUser.getStore().getSlug());
    }

    // Autenticação de login
    public AuthResponse authenticate(AuthCredentials loginRequest) {
        try {
            // Valida credenciais usando AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciais inválidas");
        }

        // Busca usuário no banco
        StoreUser storeUser = storeUserRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Gera tokens JWT
        String accessToken = jwtService.generateAccessToken(new UserDetailsImpl(storeUser));
        String refreshToken = jwtService.generateRefreshToken(new UserDetailsImpl(storeUser));

        saveRefreshToken(storeUser, refreshToken);

        return new AuthResponse(accessToken, refreshToken, storeUser.getStore().getId(), storeUser.getStore().getSlug());
    }

    // Refresh token
    public AuthResponse refreshToken(RefreshRequest request) {
        String refreshToken = request.refreshToken();

        // Extrai usuário do token
        StoreUser storeUser = storeUserRepository.findByEmail(jwtService.extractEmail(refreshToken))
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Busca o refresh token no banco
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        // Verifica expiração do refresh token
        Instant now = Instant.now();
        if (tokenEntity.getExpiryDate().isBefore(now)) {
            refreshTokenRepository.delete(tokenEntity); // Deleta token expirado
            throw new RuntimeException("Refresh token expired");
        }

        // Gera sempre um novo access token
        String newAccessToken = jwtService.generateAccessToken(new UserDetailsImpl(storeUser));

        // Rotação parcial do refresh token: só gera novo se estiver perto de expirar (ex.: 24h)
        Instant threshold = now.plusSeconds(24 * 3600); // 24h
        String newRefreshToken = refreshToken;
        if (tokenEntity.getExpiryDate().isBefore(threshold)) {
            newRefreshToken = jwtService.generateRefreshToken(new UserDetailsImpl(storeUser));
            tokenEntity.setToken(newRefreshToken); // Atualiza token
            tokenEntity.setExpiryDate(now.plusMillis(jwtService.refreshTokenExpiration)); // Atualiza validade
            refreshTokenRepository.save(tokenEntity); // Salva alterações
        }

        return new AuthResponse(newAccessToken, newRefreshToken, storeUser.getStore().getId(), storeUser.getStore().getSlug());
    }

    // Salva refresh token no banco
    private void saveRefreshToken(StoreUser storeUser, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(storeUser);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtService.refreshTokenExpiration));
        refreshTokenRepository.save(refreshToken);
    }
}
