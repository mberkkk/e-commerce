package com.microservices.product_service.product.dto.response;

import com.microservices.product_service.product.dto.ProductDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductListResponse {
    List<ProductDTO> productDTOS;
}
