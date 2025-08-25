package com.tizo.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tizo.delivery.model.dto.product.ProductDto;
import com.tizo.delivery.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/{storeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> addProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "productImage", required = false) MultipartFile productImage,
            @PathVariable String storeId
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ProductDto productDto = mapper.readValue(productJson, ProductDto.class);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addProductToStore(productDto, storeId, productImage));
    }


    @PutMapping(value = "/{storeId}/{productID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> updateProduct(@RequestPart("product") ProductDto productDto, @RequestPart(value = "productImage", required = false) MultipartFile productImage, @PathVariable String storeId, @PathVariable Long productID) throws IOException {
        ProductDto updatedProduct = productService.updateProduct(productDto, productID, storeId, productImage);
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<List<ProductDto>> getProductsByStoreId(@PathVariable String storeId) {
        List<ProductDto> product = productService.getProductByStoreId(storeId);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{storeId}/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String storeId, @PathVariable Long productId) {
        ProductDto product = productService.getProductById(storeId, productId);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{storeId}/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId, @PathVariable String storeId) throws IOException {
        boolean deleted = productService.deleteProduct(productId, storeId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/{storeId}/categories")
    public ResponseEntity<List<String>> getAllCategories(@PathVariable String storeId) {
        List<String> categories = productService.getAllCategories(storeId);
        return ResponseEntity.ok(categories);
    }
}
