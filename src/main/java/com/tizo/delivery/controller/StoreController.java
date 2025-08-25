package com.tizo.delivery.controller;

import com.tizo.delivery.model.dto.store.RegisterStoreDto;
import com.tizo.delivery.model.dto.store.ResponseStoreDto;
import com.tizo.delivery.model.dto.store.StoreProductsDto;
import com.tizo.delivery.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/signing")
    public ResponseEntity<ResponseStoreDto> signingStore(@RequestBody RegisterStoreDto registerStoreDto) {
        ResponseStoreDto store = storeService.createStore(registerStoreDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(store);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreProductsDto> findById(@PathVariable String id, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "100") Integer size) {
        StoreProductsDto store = storeService.getByID(id, page, size);
        if (store == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(store);
    }
}
