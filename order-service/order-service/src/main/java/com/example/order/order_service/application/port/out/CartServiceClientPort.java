package com.example.order.order_service.application.port.out;

import com.example.order.order_service.application.dto.CartDto;

import java.util.Optional;

public interface CartServiceClientPort {
    Optional<CartDto> getCartByUserId(String userId);

    void clearCart(String userId);
}
