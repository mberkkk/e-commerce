package com.example.order.order_service.infrastructure.persistence.client.feign;

import com.example.order.order_service.application.dto.CartDto;
import com.example.order.order_service.application.dto.CartItemDto;
import com.example.order.order_service.application.port.out.CartServiceClientPort;
import com.example.order.order_service.infrastructure.persistence.client.feign.dto.CartItemResponseDto;
import com.example.order.order_service.infrastructure.persistence.client.feign.dto.CartServiceResponse;
import com.example.order.order_service.infrastructure.persistence.client.feign.dto.FeignCartDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartServiceFeignAdapter implements CartServiceClientPort {

    private final CartServiceFeignClient cartServiceFeignClient;

    @Override
    public Optional<CartDto> getCartByUserId(String userId) {
        try {
            log.info("🌐 REAL CartService: Fetching cart for userId: {} via Feign", userId);

            CartServiceResponse response = cartServiceFeignClient.getCartByUserId(userId);

            if (response.getCartDto() != null) {
                FeignCartDto feignCartDto = response.getCartDto();

                List<CartItemDto> appCartItems = feignCartDto.getCartItems().stream()
                        .map(this::mapToApplicationDto)
                        .collect(Collectors.toList());

                BigDecimal totalPrice = appCartItems.stream()
                        .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                CartDto appCartDto = new CartDto(
                        String.valueOf(feignCartDto.getId()),
                        appCartItems,
                        totalPrice);

                log.info("🌐 REAL CartService: Successfully fetched {} items for user: {}", appCartItems.size(),
                        userId);
                return Optional.of(appCartDto);
            }

            log.warn("🌐 REAL CartService: Cart for user {} is empty or invalid", userId);
            return Optional.empty();

        } catch (Exception e) {
            log.error("🌐 REAL CartService: Error fetching cart for user: {}", userId, e);
            return Optional.empty();
        }
    }

    private CartItemDto mapToApplicationDto(CartItemResponseDto feignDto) {
        return new CartItemDto(
                String.valueOf(feignDto.getProductId()),
                feignDto.getProductName(),
                feignDto.getQuantity(),
                feignDto.getPrice());
    }

    @Override
    public void clearCart(String userId) {
        // FIXME: Cart service needs an endpoint to clear cart by userId. The current
        // one uses cartId.
        try {
            // This is a temporary and incorrect implementation.
            // We cannot clear cart by userId with the current cart-service endpoint.
            // long cartId = Long.parseLong(userId);
            // cartServiceFeignClient.clearCart(cartId);
            log.warn("🌐 REAL CartService: Clearing cart by userId is not yet implemented. Skipped for user: {}",
                    userId);
        } catch (Exception e) {
            log.error("🌐 REAL CartService: Error during placeholder clearCart for user: {}", userId, e);
        }
    }
}