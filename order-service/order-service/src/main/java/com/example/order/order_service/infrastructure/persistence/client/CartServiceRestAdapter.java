package com.example.order.order_service.infrastructure.persistence.client;

import com.example.order.order_service.application.dto.CartDto;
import com.example.order.order_service.application.dto.CartItemDto;
import com.example.order.order_service.application.port.out.CartServiceClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component; // Bu kalacak
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono; // Bu kalacak

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component // Bu kalacak
@Profile("mock") // Mock profili için bu adapter kullanılacak
public class CartServiceRestAdapter implements CartServiceClientPort {

    private final WebClient webClient; // Bu kalacak, constructor'da hala inject edilecek

    public CartServiceRestAdapter(WebClient.Builder webClientBuilder,
            @Value("${services.cart.base-url}") String cartServiceBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(cartServiceBaseUrl).build();
    }

    @Override
    public Optional<CartDto> getCartByUserId(String userId) {
        System.out.println("Mock CartService: Retrieving cart for user ID: " + userId);
        if (userId != null && !userId.isBlank()) {
            List<CartItemDto> items = Arrays.asList(
                    new CartItemDto("prod-A", "Laptop", 1, new BigDecimal("1200.00")),
                    new CartItemDto("prod-B", "Mouse", 2, new BigDecimal("25.00")));
            BigDecimal totalPrice = items.stream()
                    .map(item -> item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            CartDto cart = new CartDto("cart-" + userId, items, totalPrice);
            return Optional.of(cart);
        }
        return Optional.empty();
    }

    @Override
    public void clearCart(String userId) {
        System.out.println("Mock CartService: Clearing cart for user ID: " + userId); // In a real scenario, this would
                                                                                      // make an HTTP call to clear the
                                                                                      // cart.
    }
}