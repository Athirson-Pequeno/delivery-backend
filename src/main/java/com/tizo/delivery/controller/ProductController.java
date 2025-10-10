package com.tizo.delivery.controller;

import com.tizo.delivery.model.dto.product.ProductDto;
import com.tizo.delivery.model.exception.ErrorResponse;
import com.tizo.delivery.model.product.ProductMultipartRequest;
import com.tizo.delivery.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Adicionar produto", description = "Adiciona um novo produto à loja informada. Pode incluir imagem do produto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class),
                            examples = @ExampleObject(
                                    value = "{\n" +
                                            "  \"id\": 1,\n" +
                                            "  \"name\": \"Cachorro quente\",\n" +
                                            "  \"description\": \"Pão com salsicha, vinagrete, batata palha e carne moída\",\n" +
                                            "  \"imagePath\": \"http://localhost:8080/images/hamburguer.jpg\",\n" +
                                            "  \"category\": \"Lanches\",\n" +
                                            "  \"productSizes\": [\n" +
                                            "    { \"id\": 1, \"sizeName\": \"Médio\", \"price\": 25.5, \"size\": \"20 cm\" },\n" +
                                            "    { \"id\": 2, \"sizeName\": \"Grande\", \"price\": 35.0, \"size\": \"25 cm\" }\n" +
                                            "  ],\n" +
                                            "  \"extrasGroups\": [\n" +
                                            "    {\n" +
                                            "      \"id\": 1,\n" +
                                            "      \"name\": \"Molhos\",\n" +
                                            "      \"minSelections\": 0,\n" +
                                            "      \"maxSelections\": 3,\n" +
                                            "      \"extras\": [\n" +
                                            "        { \"id\": 1, \"name\": \"Barbecue\", \"value\": 3.5, \"limit\": 1, \"active\": true },\n" +
                                            "        { \"id\": 2, \"name\": \"Mostarda\", \"value\": 2.0, \"limit\": 1, \"active\": true }\n" +
                                            "      ]\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/{storeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> addProduct(
            @RequestHeader("Authorization") String token,
            @ModelAttribute @Parameter(
                    description = "Produto em JSON",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))
            ) ProductMultipartRequest productDTO,
            @PathVariable String storeId
    ) throws IOException {
        ProductDto createdProduct = productService.addProductToStore(productDTO.getProduct(), storeId, productDTO.getProductImage(), token);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{storeId}/{productID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> updateProduct(@RequestPart("product") ProductDto productDto, @RequestPart(value = "productImage", required = false) MultipartFile productImage, @PathVariable String storeId, @PathVariable Long productID) throws IOException {
        ProductDto updatedProduct = productService.updateProduct(productDto, productID, storeId, productImage);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<List<ProductDto>> getProductsByStoreId(@PathVariable String storeId) {
        List<ProductDto> product = productService.getProductByStoreId(storeId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{storeId}/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String storeId, @PathVariable Long productId) {
        ProductDto product = productService.getProductById(storeId, productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{storeId}/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId, @PathVariable String storeId) throws IOException {
        boolean deleted = productService.deleteProduct(productId, storeId);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{storeId}/categories")
    public ResponseEntity<List<String>> getAllCategories(@PathVariable String storeId) {
        List<String> categories = productService.getAllCategories(storeId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
