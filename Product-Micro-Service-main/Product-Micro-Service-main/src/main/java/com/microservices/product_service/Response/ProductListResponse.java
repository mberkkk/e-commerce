package com.microservices.product_service.Response;

import com.microservices.product_service.DTO.ProductDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductListResponse {
    List<ProductDTO> productDTOS;
}
