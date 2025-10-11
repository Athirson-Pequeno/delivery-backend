package com.tizo.delivery.service;

import com.hazelcast.internal.ascii.rest.HttpForbiddenException;
import com.tizo.delivery.model.dto.product.ProductDto;
import com.tizo.delivery.model.product.Product;
import com.tizo.delivery.model.product.ProductExtras;
import com.tizo.delivery.model.product.ProductExtrasGroup;
import com.tizo.delivery.model.store.Store;
import com.tizo.delivery.model.store.StoreUser;
import com.tizo.delivery.repository.ProductRepository;
import com.tizo.delivery.repository.StoreRepository;
import com.tizo.delivery.util.SlugGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    private final SlugGenerator slugGenerator;
    private final JwtService jwtService;

    public ProductService(ProductRepository productRepository,
                          StoreRepository storeRepository,
                          SlugGenerator slugGenerator,
                          JwtService jwtService) {
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
        this.slugGenerator = slugGenerator;
        this.jwtService = jwtService;
    }


    @Transactional
    public ProductDto addProductToStore(ProductDto productDto, String storeId, MultipartFile productImage, String token) throws IOException {

        Store store = validateStore(storeId, token);

        Product product = new Product();
        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setCategory(productDto.category());
        product.setProductSize(productDto.productSizes());

        product.getProductSize().forEach(size -> size.setProduct(product));

        product.setStore(store);

        if (productImage != null && !productImage.isEmpty()) {
            product.setImagePath(createFileUrl(productImage, store.getSlug()));
        }

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
                    extra.setValue(extraDto.value());
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
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado, id: " + productId));

        if (!product.getStore().getId().equals(storeId)) {
            throw new HttpForbiddenException();
        }
        return new ProductDto(product);
    }

    public List<ProductDto> getProductByStoreId(String storeId) {

        return productRepository.getProductsByStoreId(storeId)
                .stream()
                .map(ProductDto::new)
                .toList();

    }

    public ProductDto updateProduct(ProductDto productDto, Long productId, String storeId, MultipartFile productImage, String token) throws IOException {

        validateStore(storeId, token);

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new EntityNotFoundException("Produto não encontrado, id: " + productId));

        if (!product.getStore().getId().equals(storeId)) {
            throw new HttpForbiddenException();
        }

        product.setName(productDto.name());
        product.setDescription(productDto.description());
        product.setCategory(productDto.category());

        if (productImage != null && !productImage.isEmpty()) {
            product.setImagePath(createFileUrl(productImage, product.getStore().getSlug()));
        }

        Product updatedProduct = productRepository.save(product);

        return new ProductDto(updatedProduct);
    }

    public boolean deleteProduct(Long productId, String storeId, String token) {

        validateStore(storeId, token);

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new EntityNotFoundException("Produto não encontrado, id: " + productId));

        if (!product.getStore().getId().equals(storeId)) {
            throw new HttpForbiddenException();
        }

        if (productRepository.existsById(productId)) {
            if (product.getImagePath() != null) {
                try {
                    Path destination = Paths.get("uploads/" + product.getImagePath());
                    Files.delete(destination);
                } catch (IOException e) {
                    productRepository.deleteById(productId);
                    return true;
                }
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

    private Store validateStore(String storeId, String token) {
        String userEmail = jwtService.extractEmail(token);

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Loja não encontrada, id: " + storeId));

        if (store.getUsers().stream().map(StoreUser::getEmail).noneMatch(email -> email.equals(userEmail))) {
            throw new HttpForbiddenException();
        }

        return store;
    }
}
