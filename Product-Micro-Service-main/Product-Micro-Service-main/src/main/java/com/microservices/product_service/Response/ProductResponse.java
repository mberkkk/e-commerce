package com.microservices.product_service.Response;

import com.microservices.product_service.DTO.ProductDTO;
import lombok.Data;

@Data
public class ProductResponse {
    private ProductDTO productDTO;
}
