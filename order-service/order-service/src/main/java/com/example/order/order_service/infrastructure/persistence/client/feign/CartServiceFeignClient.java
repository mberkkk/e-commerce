package com.example.order.order_service.infrastructure.persistence.client.feign;

import com.example.order.order_service.infrastructure.persistence.client.feign.dto.CartServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

@FeignClient(name = "cart-service", url = "${services.cart.base-url}")
public interface CartServiceFeignClient {

    @GetMapping("/api/cart/user/{userId}")
    CartServiceResponse getCartByUserId(@PathVariable("userId") String userId);

    @DeleteMapping("/api/cart/{cartId}/clear")
    void clearCart(@PathVariable("cartId") Long cartId);
}