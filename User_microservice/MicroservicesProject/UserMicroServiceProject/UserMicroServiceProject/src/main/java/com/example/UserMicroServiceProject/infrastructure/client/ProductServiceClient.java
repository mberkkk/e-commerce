package com.example.UserMicroServiceProject.infrastructure.client;

import com.example.UserMicroServiceProject.infrastructure.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "product-service",
        url = "${external-services.product-service.url}",
        path = "/api/products",
        configuration = FeignConfig.class
)public interface ProductServiceClient {

    @GetMapping("/{productId}")
    ProductDTO getProduct(@PathVariable("productId") String productId);

    @GetMapping("/{productId}/availability")
    boolean checkProductAvailability(@PathVariable("productId") String productId);

    record ProductDTO(
            String id,
            String name,
            String description,
            double price,
            int stockQuantity,
            boolean available
    ) {}
}