package com.microservices.cart_service.Listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.cart_service.Event.OrderCreatedEvent;
import com.microservices.cart_service.Service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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
    public void handleUserRegistered(ConsumerRecord<String, String> record) {
        try {
            String value = record.value();
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(value);
            String userId = node.get("userId").asText();
            if (userId != null) {
                cartService.createCart(userId);
                log.info("Cart created for new user: {}", userId);
            } else {
                log.warn("user-registered event'te userId bulunamadı: {}", value);
            }
        } catch (Exception e) {
            log.error("Error creating cart for new user from user-registered event", e);
        }
    }
}