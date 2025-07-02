package com.example.UserMicroServiceProject.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cart-service",url = "${external-services.cart-service.url}", path = "/api/cart")
public interface CartServiceClient {

    @GetMapping("/{userId}")
    CartDTO getCart(@PathVariable("userId") String userId);

    record CartDTO(
            String id,
            String userId,
            java.util.List<CartItemDTO> items,
            double totalAmount
    ) {}

    record CartItemDTO(
            String productId,
            String productName,
            int quantity,
            double price
    ) {}
}
