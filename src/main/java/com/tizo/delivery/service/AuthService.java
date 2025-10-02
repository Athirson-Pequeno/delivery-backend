package com.tizo.delivery.service;

import com.tizo.delivery.exception.exceptions.UnauthorizedException;
import com.tizo.delivery.model.auth.RefreshToken;
import com.tizo.delivery.model.store.Store;
import com.tizo.delivery.model.store.StoreUser;
import com.tizo.delivery.model.auth.UserDetailsImpl;
import com.tizo.delivery.model.dto.auth.AuthCredentialsDto;
import com.tizo.delivery.model.dto.auth.AuthResponseDto;
import com.tizo.delivery.model.dto.auth.RefreshRequestDto;
import com.tizo.delivery.model.enums.StoreUserRole;
import com.tizo.delivery.repository.RefreshTokenRepository;
import com.tizo.delivery.repository.StoreRepository;
import com.tizo.delivery.repository.StoreUserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    public AuthResponseDto registerManager(AuthCredentialsDto registerRequest, String storeId) {
        // Delegando para método genérico com role MANAGER
        return registerManager(registerRequest, storeId, StoreUserRole.MANAGER);
    }

    // Registro de funcionário
    public AuthResponseDto registerEmployee(AuthCredentialsDto registerRequest, String storeId) {
        // Delegando para método genérico com role EMPLOYEE
        return registerManager(registerRequest, storeId, StoreUserRole.EMPLOYEE);
    }

    // Método genérico de registro
    public AuthResponseDto registerManager(AuthCredentialsDto registerRequest, String storeId, StoreUserRole storeUserRole) {
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
        return new AuthResponseDto(accessToken, refreshToken, storeUser.getStore().getId(), storeUser.getStore().getSlug());
    }

    // Autenticação de login
    @Transactional
    public AuthResponseDto authenticate(AuthCredentialsDto loginRequest) {
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

        // Invalida todos tokens anteriores do usuário (garante apenas 1 ativo)
        refreshTokenRepository.deleteByUser(storeUser);
        refreshTokenRepository.flush();

        saveRefreshToken(storeUser, refreshToken);

        return new AuthResponseDto(accessToken, refreshToken, storeUser.getStore().getId(), storeUser.getStore().getSlug());
    }

    // Refresh token
    @Transactional
    public AuthResponseDto refreshToken(RefreshRequestDto request) {
        String oldRefreshToken = request.refreshToken();
        Instant now = Instant.now();

        // 1. Verifica se o refresh token existe no banco
        RefreshToken tokenEntity = refreshTokenRepository.findByToken(oldRefreshToken)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        // 2. Verifica se já expirou
        if (tokenEntity.getExpiryDate().isBefore(now)) {
            refreshTokenRepository.delete(tokenEntity); // remove token expirado
            throw new UnauthorizedException("Unauthorized");
        }

        // 3. Busca usuário associado
        StoreUser storeUser = tokenEntity.getUser();
        if (storeUser == null) {
            refreshTokenRepository.delete(tokenEntity); // token corrompido
            throw new UnauthorizedException("Unauthorized");
        }

        // 4. Gera novo access token
        UserDetailsImpl userDetails = new UserDetailsImpl(storeUser);
        String newAccessToken = jwtService.generateAccessToken(userDetails);

        // 5. Rotação total do refresh token
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        // Invalida todos tokens anteriores do usuário (garante apenas 1 ativo)
        refreshTokenRepository.deleteByUser(storeUser);
        refreshTokenRepository.flush();

        // Salva o novo refresh token
        RefreshToken newTokenEntity = new RefreshToken();
        newTokenEntity.setToken(newRefreshToken);
        newTokenEntity.setUser(storeUser);
        newTokenEntity.setExpiryDate(now.plusMillis(jwtService.refreshTokenExpiration));
        refreshTokenRepository.save(newTokenEntity);

        // 6. Retorna novos tokens
        return new AuthResponseDto(
                newAccessToken,
                newRefreshToken,
                storeUser.getStore().getId(),
                storeUser.getStore().getSlug()
        );
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
