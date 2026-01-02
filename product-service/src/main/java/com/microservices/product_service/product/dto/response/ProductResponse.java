package com.microservices.product_service.product.dto.response;

import com.microservices.product_service.product.dto.ProductDTO;
import lombok.Data;

@Data
public class ProductResponse {
    private ProductDTO productDTO;
}
