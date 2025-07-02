package com.microservices.cart_service.Service;

import com.microservices.cart_service.Entity.CartItem;
import com.microservices.cart_service.Request.AddToCartRequest;
import com.microservices.cart_service.Response.CartResponse;

import java.util.List;

public interface CartService {
    CartResponse getCartById(Long cartId);

    CartResponse getCartByUserId(String userId);

    CartResponse createCart(String userId);

    CartResponse addItemsToCart(String userId, List<AddToCartRequest.CartItemRequest> cartItems);

    CartResponse updateCartItem(String userId, CartItem cartItem);

    CartResponse removeCartItem(String userId, CartItem cartItem);

    void clearCart(String userId);

    void publishAbandonedCarts();
}
