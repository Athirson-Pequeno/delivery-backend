package com.tizo.delivery.service;

import com.tizo.delivery.model.Product;
import com.tizo.delivery.model.Store;
import com.tizo.delivery.model.dto.ProductDto;
import com.tizo.delivery.repository.ProductRepository;
import com.tizo.delivery.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final Path uploadDirectory = Paths.get("uploads/images/products");

    public ProductService(ProductRepository productRepository, StoreRepository storeRepository) throws IOException {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;

        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }
    }

    public ProductDto addProductToStore(ProductDto productDto, String storeId, MultipartFile productImage) throws IOException {
        Product product = new Product();
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setCategory(productDto.category());

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        if (productImage != null && !productImage.isEmpty()) {
            product.setImagePath(createFileUrl(productImage));
        }
        product.setStore(store);

        Product createdProduct = productRepository.save(product);

        return new ProductDto(createdProduct);

    }

    public ProductDto getProductById(String storeId, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (!product.getStore().getId().equals(storeId)) {
            throw new RuntimeException("Product does not belong to this store");
        }
        return new ProductDto(product);
    }


    public List<ProductDto> getProductByStoreId(String storeId) {

        return productRepository.getProductsByStoreId(storeId)
                .stream()
                .map(ProductDto::new)
                .toList();

    }

    public ProductDto updateProduct(ProductDto productDto, Long productID, String storeId, MultipartFile productImage) throws IOException {
        Product product = productRepository.findById(productID).orElseThrow(() -> new RuntimeException("Product not found with id: " + productID));

        if (!product.getStore().getId().equals(storeId)) {
            throw new RuntimeException("Product does not belong to the specified store.");
        }

        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setCategory(productDto.category());

        if (productImage != null && !productImage.isEmpty()) {
            product.setImagePath(createFileUrl(productImage));
        }

        Product updatedProduct = productRepository.save(product);

        return new ProductDto(updatedProduct);
    }

    public boolean deleteProduct(Long productId, String storeId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        if (!product.getStore().getId().equals(storeId)) {
            throw new RuntimeException("Product does not belong to the specified store.");
        }

        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }

    private String createFileUrl(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destination = uploadDirectory.resolve(fileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return "images/products/" + fileName;
    }

    public List<String> getAllCategories(String storeId) {
        return getProductByStoreId(storeId).stream()
                .map(ProductDto::category)
                .distinct()
                .toList();

    }
}
