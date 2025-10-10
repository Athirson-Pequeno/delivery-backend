package com.tizo.delivery.model.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tizo.delivery.model.dto.product.ProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

public class ProductMultipartRequest {

    @Schema(
            description = "Produto em JSON",
            type = "object",
            implementation = ProductDto.class,
            example = "{\n" +
                    "  \"name\": \"Cachorro quente\",\n" +
                    "  \"description\": \"Pão com salsicha, vinagrete...\",\n" +
                    "  \"category\": \"Lanches\",\n" +
                    "  \"productSizes\": [\n" +
                    "    {\"sizeName\": \"Médio\", \"price\": 25.5, \"size\": \"20 cm\"}\n" +
                    "  ],\n" +
                    "  \"extrasGroups\": []\n" +
                    "}"
    )
    private String productString;
    private MultipartFile productImage;

    public ProductDto getProduct() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(this.productString, ProductDto.class);
    }
    public void setProduct(String product) { this.productString = product; }

    public MultipartFile getProductImage() { return productImage; }
    public void setProductImage(MultipartFile productImage) { this.productImage = productImage; }
}
