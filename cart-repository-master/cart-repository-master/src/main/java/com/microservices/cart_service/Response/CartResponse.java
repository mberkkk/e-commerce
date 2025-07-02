package com.microservices.cart_service.Response;

import com.microservices.cart_service.DTO.CartDto;
import lombok.Data;

@Data
public class CartResponse {
    private CartDto cartDto;
}
