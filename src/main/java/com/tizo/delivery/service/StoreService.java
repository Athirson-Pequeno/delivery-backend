package com.tizo.delivery.service;

import com.tizo.delivery.model.store.Store;
import com.tizo.delivery.model.store.StoreUser;
import com.tizo.delivery.model.dto.product.ProductDto;
import com.tizo.delivery.model.dto.store.RegisterStoreDto;
import com.tizo.delivery.model.dto.store.ResponseStoreDto;
import com.tizo.delivery.model.dto.store.StoreProductsDto;
import com.tizo.delivery.model.enums.StoreUserRole;
import com.tizo.delivery.repository.ProductRepository;
import com.tizo.delivery.repository.StoreRepository;
import com.tizo.delivery.repository.StoreUserRepository;
import com.tizo.delivery.util.SlugGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    private final PasswordEncoder passwordEncoder;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final StoreUserRepository storeUserRepository;
    private final SlugGenerator slugGenerator;

    public StoreService(SlugGenerator slugGenerator, PasswordEncoder passwordEncoder, StoreRepository storeRepository, ProductRepository productRepository, StoreUserRepository storeUserRepository) {
        this.slugGenerator = slugGenerator;
        this.passwordEncoder = passwordEncoder;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.storeUserRepository = storeUserRepository;
    }

    public ResponseStoreDto createStore(RegisterStoreDto registerStoreDto) {
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

        return new ResponseStoreDto(store);
    }

    public StoreProductsDto getByID(String id, Integer page, Integer size) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "category"));
        Page<ProductDto> productPage = productRepository.findByStoreId(id, pageable).map(ProductDto::new);

        return new StoreProductsDto(store, productPage);
    }

    public StoreProductsDto getBySlug(String slug, Integer page, Integer size) {
        Store store = storeRepository.getStoreBySlug(slug).orElseThrow(() -> new RuntimeException("Store not found with slug: " + slug));

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> productPage = productRepository.findByStoreId(store.getId(), pageable).map(ProductDto::new);

        return new StoreProductsDto(store, productPage);
    }
}