package com.microservices.cart_service.Controller;

import com.microservices.cart_service.Request.AddToCartRequest;
import com.microservices.cart_service.Request.MultipleItemsRequest;
import com.microservices.cart_service.Request.SingleItemRequest;
import com.microservices.cart_service.Response.CartResponse;
import com.microservices.cart_service.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/cart")
@RestController
@RequiredArgsConstructor
public class CartControllerImpl implements CartController {
    private final CartService cartService;

    @Override
    @GetMapping("/{id}")
    public CartResponse getCart(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    @Override
    @GetMapping("/user/{userId}")
    public CartResponse getCartByUserId(@PathVariable String userId) {
        return cartService.getCartByUserId(userId);
    }

    @Override
    @PostMapping("/{userId}/create")
    public CartResponse createCart(@PathVariable String userId) {
        return cartService.createCart(userId);
    }

    @Override
    @PostMapping("/{userId}/items")
    public CartResponse addItemsToCart(@PathVariable String userId, @RequestBody AddToCartRequest request) {
        return cartService.addItemsToCart(userId, request.getItems());
    }

    @Override
    @PutMapping("/{cartId}/items")
    public CartResponse updateCartItem(@PathVariable("cartId") String cartId, @RequestBody SingleItemRequest request) {
        return cartService.updateCartItem(cartId, request.getItem());
    }

    @Override
    @DeleteMapping("/{cartId}/items")
    public CartResponse removeCartItem(@PathVariable("cartId") String cartId, @RequestBody SingleItemRequest request) {
        return cartService.removeCartItem(cartId, request.getItem());
    }

    @Override
    @DeleteMapping("/{cartId}/clear")
    public void clearCart(@PathVariable("cartId") String cartId) {
        cartService.clearCart(cartId);
    }
}
