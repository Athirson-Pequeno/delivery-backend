package com.tizo.delivery.controller;

import com.tizo.delivery.docs.CommonApiResponses;
import com.tizo.delivery.docs.ExampleJsons;
import com.tizo.delivery.model.dto.product.ProductDto;
import com.tizo.delivery.model.exception.ErrorResponse;
import com.tizo.delivery.model.product.ProductMultipartRequest;
import com.tizo.delivery.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Tag(name = "Produtos", description = "Endpoints para gerenciamento de produtos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Adicionar produto", description = "Adiciona um novo produto à loja informada. Pode incluir imagem do produto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class), examples = @ExampleObject(value = ExampleJsons.PRODUCT))),
    })
    @CommonApiResponses
    @PostMapping(value = "/{storeId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> addProduct(
            @RequestHeader("Authorization") String token,
            @ModelAttribute ProductMultipartRequest productDTO,
            @PathVariable String storeId
    ) throws IOException {
        ProductDto createdProduct = productService.addProductToStore(productDTO.getProduct(), storeId, productDTO.getProductImage(), token);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualizar produto", description = "Atualiza as informações de um produto existente, incluindo a imagem, se fornecida.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ProductDto.class),
                    examples = @ExampleObject(value = ExampleJsons.PRODUCT))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @CommonApiResponses
    @PutMapping(value = "/{storeId}/{productID}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> updateProduct(
            @RequestHeader("Authorization") String token,
            @ModelAttribute ProductMultipartRequest productDTO,
            @PathVariable String storeId,
            @PathVariable Long productID) throws IOException {
        ProductDto updatedProduct = productService.updateProduct(productDTO.getProduct(), productID, storeId, productDTO.getProductImage(), token);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @Operation(
            summary = "Listar produtos da loja",
            description = "Retorna todos os produtos cadastrados para uma loja específica."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de produtos retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class),
                            examples = @ExampleObject(value = ExampleJsons.PRODUCT_LIST))),
            @ApiResponse(responseCode = "404", description = "Loja não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))

    })
    @GetMapping("/{storeId}")
    public ResponseEntity<List<ProductDto>> getProductsByStoreId(@PathVariable String storeId) {
        List<ProductDto> product = productService.getProductByStoreId(storeId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @Operation(
            summary = "Buscar produto por ID",
            description = "Retorna um produto específico de uma loja pelo seu ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Produto encontrado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDto.class),
                            examples = @ExampleObject(value = ExampleJsons.PRODUCT))),
            @ApiResponse(responseCode = "404", description = "Produto ou loja não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{storeId}/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String storeId, @PathVariable Long productId) {
        ProductDto product = productService.getProductById(storeId, productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @Operation(summary = "Excluir produto", description = "Remove um produto de uma loja. Retorna 204 se for excluído com sucesso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto ou loja não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @CommonApiResponses
    @DeleteMapping("/{storeId}/{productId}")
    public ResponseEntity<?> deleteProduct(@RequestHeader("Authorization") String token, @PathVariable Long productId, @PathVariable String storeId) throws IOException {
        boolean deleted = productService.deleteProduct(productId, storeId, token);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
