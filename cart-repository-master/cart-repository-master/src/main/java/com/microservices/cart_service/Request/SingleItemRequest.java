package com.microservices.cart_service.Request;

import com.microservices.cart_service.Entity.CartItem;
import lombok.Data;

@Data
public class SingleItemRequest {
    private CartItem item;
}
