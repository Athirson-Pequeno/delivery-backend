package com.tizo.delivery.service;

import com.tizo.delivery.model.*;
import com.tizo.delivery.model.dto.product.ProductDto;
import com.tizo.delivery.repository.ProductRepository;
import com.tizo.delivery.repository.StoreRepository;
import com.tizo.delivery.util.SlugGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final SlugGenerator slugGenerator;

    public ProductService(ProductRepository productRepository, StoreRepository storeRepository, SlugGenerator slugGenerator) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.slugGenerator = slugGenerator;
    }

    @Transactional
    public ProductDto addProductToStore(ProductDto productDto, String storeId, MultipartFile productImage) throws IOException {
        Product product = new Product();
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        product.setCategory(productDto.category());
        product.setProductSize(productDto.productSizes());

        if (product.getProductSize() == null) {
            List<ProductSize> defaultSize = new ArrayList<>();
            defaultSize.add(new ProductSize(productDto.price()));
            defaultSize.forEach(System.out::println);
            product.setProductSize(defaultSize);
        }

        product.getProductSize().forEach(size -> size.setProduct(product));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        product.setStore(store);

        if (productImage != null && !productImage.isEmpty()) {
            product.setImagePath(createFileUrl(productImage, store.getSlug()));
        }

        // Montar grupos + extras
        if (productDto.extrasGroups() != null && !productDto.extrasGroups().isEmpty()) {
        List<ProductExtrasGroup> groups = productDto.extrasGroups().stream().map(groupDto -> {
            ProductExtrasGroup group = new ProductExtrasGroup();
            group.setName(groupDto.name());
            group.setMinSelections(groupDto.minSelections());
            group.setMaxSelections(groupDto.maxSelections());
            group.setProduct(product);

            List<ProductExtras> extras = groupDto.extras().stream().map(extraDto -> {
                ProductExtras extra = new ProductExtras();
                extra.setName(extraDto.name());
                extra.setValue(extraDto.value() != null ? BigDecimal.valueOf(extraDto.value()) : null);
                extra.setLimit(extraDto.limit());
                extra.setActive(extraDto.active() != null ? extraDto.active() : true);
                extra.setProduct(product);
                extra.setExtraGroup(group);
                return extra;
            }).toList();

            group.setExtras(extras);
            return group;
        }).toList();

            product.setExtrasGroups(groups);
        }

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
            product.setImagePath(createFileUrl(productImage, product.getStore().getSlug()));
        }

        Product updatedProduct = productRepository.save(product);

        return new ProductDto(updatedProduct);
    }

    public boolean deleteProduct(Long productId, String storeId) throws IOException {

        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        if (!product.getStore().getId().equals(storeId)) {
            throw new RuntimeException("Product does not belong to the specified store.");
        }

        if (productRepository.existsById(productId)) {
            if (product.getImagePath() != null) {
                Path destination = Paths.get("uploads/" + product.getImagePath());
                Files.delete(destination);
            }
            productRepository.deleteById(productId);
            return true;
        }
        return false;
    }

    private String createFileUrl(MultipartFile file, String storeSlug) throws IOException {
        Path uploadDirectory = Paths.get("uploads/images/products/" + storeSlug);

        if (!Files.exists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }

        String fileName = UUID.randomUUID() + "_" + slugGenerator.generateSlug(file.getOriginalFilename());
        Path destination = uploadDirectory.resolve(fileName);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return "images/products/" + storeSlug + "/" + fileName;
    }

    public List<String> getAllCategories(String storeId) {
        return getProductByStoreId(storeId).stream()
                .map(ProductDto::category)
                .distinct()
                .toList();

    }
}
