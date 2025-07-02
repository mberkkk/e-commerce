package com.microservices.cart_service.Controller;

import com.microservices.cart_service.Request.AddToCartRequest;
import com.microservices.cart_service.Request.MultipleItemsRequest;
import com.microservices.cart_service.Request.SingleItemRequest;
import com.microservices.cart_service.Response.CartResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CartController {
    CartResponse getCart(@PathVariable("id") Long id);

    CartResponse getCartByUserId(@PathVariable("userId") String userId);

    CartResponse createCart(@PathVariable("id") String id);

    CartResponse addItemsToCart(@PathVariable("userId") String userId, @RequestBody AddToCartRequest request);

    CartResponse updateCartItem(@PathVariable("id") String id, @RequestBody SingleItemRequest request);

    CartResponse removeCartItem(@PathVariable("id") String id, @RequestBody SingleItemRequest request);

    void clearCart(@PathVariable("id") String id);
}
