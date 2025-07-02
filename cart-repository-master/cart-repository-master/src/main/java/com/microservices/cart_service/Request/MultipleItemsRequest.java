package com.microservices.cart_service.Request;

import com.microservices.cart_service.Entity.CartItem;
import lombok.Data;

import java.util.List;

@Data
public class MultipleItemsRequest {
    private List<CartItem> items;
}
