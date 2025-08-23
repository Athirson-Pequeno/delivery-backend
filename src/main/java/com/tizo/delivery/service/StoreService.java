package com.tizo.delivery.service;

import com.tizo.delivery.model.Store;
import com.tizo.delivery.model.dto.ProductDto;
import com.tizo.delivery.model.dto.RegisterStoreDto;
import com.tizo.delivery.model.dto.ResponseStoreDto;
import com.tizo.delivery.model.dto.StoreProductsDto;
import com.tizo.delivery.repository.ProductRepository;
import com.tizo.delivery.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final PasswordEncoder passwordEncoder;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    public StoreService(PasswordEncoder passwordEncoder, StoreRepository storeRepository, ProductRepository productRepository) {
        this.passwordEncoder = passwordEncoder;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    public ResponseStoreDto createStore(RegisterStoreDto registerStoreDto) {
        Store store = new Store();
        store.setName(registerStoreDto.name());
        store.setEmail(registerStoreDto.email());
        store.setPassword(passwordEncoder.encode(registerStoreDto.password()));
        store.setAddress(registerStoreDto.address());
        store.setPhoneNumber(registerStoreDto.phoneNumber());

        storeRepository.save(store);

        return new ResponseStoreDto(store);
    }

    public StoreProductsDto getByID(String id, Integer page, Integer size) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + id));

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> productPage = productRepository.findByStoreId(id, pageable).map(ProductDto::new);

        if (productPage.isEmpty()) {
            throw new RuntimeException("No products found for store with id: " + id);
        }

        return new StoreProductsDto(store, productPage);
    }
}
