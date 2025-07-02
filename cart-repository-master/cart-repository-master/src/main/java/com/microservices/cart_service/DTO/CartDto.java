package com.microservices.cart_service.DTO;

import com.microservices.cart_service.Entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto extends BaseDTO {
    private long id;
    private String userId;
    private List<CartItem> cartItems;
}
