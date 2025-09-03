package com.microservices.cart_service.Listener;

import com.microservices.cart_service.Event.OrderCreatedEvent;
import com.microservices.cart_service.Event.UserRegisteredEvent;
import com.microservices.cart_service.Service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartEventListener {

    private final CartService cartService;

    @KafkaListener(topics = "ORDER_CREATED", groupId = "cart-service-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received order-created event JSON: {}", event);
        try {
            //ObjectMapper mapper = new ObjectMapper();
            //OrderCreatedEvent event = mapper.readValue(eventJson, OrderCreatedEvent.class);
            // Artık kendi OrderCreatedEvent class'ınızı kullanabilirsiniz
            cartService.clearCart(event.getUserId());
            log.info("Cart cleared for user: {}", event.getUserId());
        } catch (Exception e) {
            log.error("Error processing order-created event", e);
        }
    }

    @KafkaListener(topics = "user-registered", groupId = "cart-service-group")
    public void handleUserRegistered(UserRegisteredEvent event) {
        try {
            if (event.getUserId() != null) {
                cartService.createCart(event.getUserId());
                log.info("Cart created for new user: {}", event.getUserId());
            } else {
                log.warn("user-registered event'te userId bulunamadı: {}", event);
            }
        } catch (Exception e) {
            log.error("Error creating cart for new user from user-registered event", e);
        }
    }
}