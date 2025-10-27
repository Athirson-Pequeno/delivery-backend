package com.tizo.delivery.service;

import com.tizo.delivery.exception.exceptions.EmailAlreadyExistsException;
import com.tizo.delivery.model.auth.RefreshToken;
import com.tizo.delivery.model.auth.UserDetailsImpl;
import com.tizo.delivery.model.dto.auth.AuthResponseDto;
import com.tizo.delivery.model.dto.auth.StoreSigingResponseDto;
import com.tizo.delivery.model.dto.product.ProductDto;
import com.tizo.delivery.model.dto.store.RegisterStoreDto;
import com.tizo.delivery.model.dto.store.ResponseStoreDto;
import com.tizo.delivery.model.dto.store.StoreProductsDto;
import com.tizo.delivery.model.enums.StoreUserRole;
import com.tizo.delivery.model.store.Store;
import com.tizo.delivery.model.store.StoreUser;
import com.tizo.delivery.repository.ProductRepository;
import com.tizo.delivery.repository.RefreshTokenRepository;
import com.tizo.delivery.repository.StoreRepository;
import com.tizo.delivery.repository.StoreUserRepository;
import com.tizo.delivery.util.SlugGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class StoreService {

    private final PasswordEncoder passwordEncoder;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final StoreUserRepository storeUserRepository;
    private final SlugGenerator slugGenerator;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public StoreService(SlugGenerator slugGenerator, RefreshTokenRepository refreshTokenRepository,JwtService jwtService, PasswordEncoder passwordEncoder, StoreRepository storeRepository, ProductRepository productRepository, StoreUserRepository storeUserRepository) {
        this.slugGenerator = slugGenerator;
        this.passwordEncoder = passwordEncoder;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.storeUserRepository = storeUserRepository;
    }

    public StoreSigingResponseDto createStore(RegisterStoreDto registerStoreDto) {

        if (storeUserRepository.existsByEmail(registerStoreDto.email())) {
            throw new EmailAlreadyExistsException("O email '" + registerStoreDto.email() + "' já está cadastrado.");
        }

        Store store = new Store();
        store.setName(registerStoreDto.name());
        store.setSlug(slugGenerator.generateStoreSlug(registerStoreDto.name()));
        store.setAddress(registerStoreDto.address());
        store.setPhoneNumber(registerStoreDto.phoneNumber());

        storeRepository.save(store);

        StoreUser storeUser = new StoreUser();
        storeUser.setEmail(registerStoreDto.email());
        storeUser.setPassword(passwordEncoder.encode(registerStoreDto.password()));
        storeUser.setRole(StoreUserRole.OWNER);
        storeUser.setStore(store);

        storeUserRepository.save(storeUser);

        // Cria tokens JWT
        UserDetailsImpl userDetails = new UserDetailsImpl(storeUser);
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Salva refresh token no banco
        saveRefreshToken(storeUser, refreshToken);

        AuthResponseDto authResponseDto = new AuthResponseDto(accessToken, refreshToken, storeUser.getStore().getId(), storeUser.getStore().getSlug());

        return new StoreSigingResponseDto(store, authResponseDto);
    }

    // Salva refresh token no banco
    private void saveRefreshToken(StoreUser storeUser, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(storeUser);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtService.refreshTokenExpiration));
        refreshTokenRepository.save(refreshToken);
    }

    public StoreProductsDto getByID(String storeId, Integer page, Integer size) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Loja não encontrada, storeId: " + storeId));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "category"));
        Page<ProductDto> productPage = productRepository.findByStoreId(storeId, pageable).map(ProductDto::new);

        return new StoreProductsDto(store, productPage);
    }

    public StoreProductsDto getBySlug(String slug, Integer page, Integer size) {
        Store store = storeRepository.getStoreBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Loja não encontrada, storeSlug: " + slug));

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> productPage = productRepository.findByStoreId(store.getId(), pageable).map(ProductDto::new);

        return new StoreProductsDto(store, productPage);
    }
}