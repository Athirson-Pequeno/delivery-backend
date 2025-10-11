package com.tizo.delivery.controller;

import com.tizo.delivery.docs.ExampleJsons;
import com.tizo.delivery.model.dto.store.RegisterStoreDto;
import com.tizo.delivery.model.dto.store.ResponseStoreDto;
import com.tizo.delivery.model.dto.store.StoreProductsDto;
import com.tizo.delivery.model.exception.ErrorResponse;
import com.tizo.delivery.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store")
@Tag(name = "Lojas", description = "Endpoints para gerenciamento de lojas e produtos")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(summary = "Buscar loja por ID", description = "Retorna os dados de uma loja e seus produtos pelo ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Loja encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = ExampleJsons.STORE_RESPONSE))
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<StoreProductsDto> findById(
            @PathVariable String id,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "50") @Max(50) Integer size) {
        StoreProductsDto store = storeService.getByID(id, page, size);
        if (store == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(store, HttpStatus.OK);
    }

    @Operation(summary = "Buscar loja por slug", description = "Retorna os dados de uma loja e seus produtos pelo slug")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Loja encontrada",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = ExampleJsons.STORE_RESPONSE))
            ),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/by-slug/{slug}")
    public ResponseEntity<StoreProductsDto> findBySlug(@PathVariable String slug, @RequestParam(defaultValue = "0") @Min(0) Integer page, @RequestParam(defaultValue = "50") @Max(50) Integer size) {
        StoreProductsDto store = storeService.getBySlug(slug, page, size);
        if (store == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(store, HttpStatus.OK);
    }
}
