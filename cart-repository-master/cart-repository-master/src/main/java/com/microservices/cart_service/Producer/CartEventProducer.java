package com.microservices.cart_service.Producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.microservices.cart_service.Event.CartAbandonedEvent;
import com.microservices.cart_service.Event.ItemAddedToCartEvent;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j

public class CartEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishItemAddedToCart(String userId, Long productId, Integer quantity) {
        ItemAddedToCartEvent event = ItemAddedToCartEvent.builder()
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send("item-added-to-cart", event);
        log.info("Item added to cart event published: userId={}, productId={}", userId, productId);
    }

    public void publishCartAbandoned(String userId) {
        CartAbandonedEvent event = CartAbandonedEvent.builder()
                .userId(userId)
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send("cart-abandoned", event);
        log.info("Cart abandoned event published for user: {}", userId);
    }
}
