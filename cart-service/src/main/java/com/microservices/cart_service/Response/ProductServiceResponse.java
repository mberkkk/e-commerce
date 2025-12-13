package com.microservices.cart_service.Response;

import com.microservices.cart_service.DTO.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductServiceResponse {
    private ProductDto productDTO; // Product Service'ten gelen field ismi
}