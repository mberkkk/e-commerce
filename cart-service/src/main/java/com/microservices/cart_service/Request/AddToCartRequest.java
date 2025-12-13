package com.microservices.cart_service.Request;

import lombok.Data;

import java.util.List;

@Data
public class AddToCartRequest {
    private List<CartItemRequest> items;

    @Data
    public static class CartItemRequest {
        private Long productId;
        private Integer quantity;
    }
}