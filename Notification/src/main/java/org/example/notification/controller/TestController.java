package org.example.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.notification.dto.event.CartAbandonedEvent;
import org.example.notification.dto.event.OrderCreatedEvent;
import org.example.notification.dto.event.ProductStockUpdatedEvent;
import org.example.notification.dto.event.UserRegisteredEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Tag(name = "Test Controller", description = "Test endpoints for triggering Kafka events")
public class TestController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/user-registered")
    @Operation(summary = "Trigger user registered event")
    public ResponseEntity<String> triggerUserRegistered() {
        UserRegisteredEvent event = new UserRegisteredEvent();
        event.setUserId("test-user-1");
        event.setEmail("test@example.com");
        event.setUsername("testuser");
        kafkaTemplate.send("user-registered", "user-registered", event);
        return ResponseEntity.ok("User registered event triggered");
    }

    @PostMapping("/order-created")
    @Operation(summary = "Trigger order created event")
    public ResponseEntity<String> triggerOrderCreated() {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId("test-order-1");
        event.setUserId("test-user-1");
        event.setEmail("test@example.com");
        event.setTotalAmount(new BigDecimal("99.99"));
        kafkaTemplate.send("order-created", "order-created", event);
        return ResponseEntity.ok("Order created event triggered");
    }

    @PostMapping("/cart-abandoned")
    @Operation(summary = "Trigger cart abandoned event")
    public ResponseEntity<String> triggerCartAbandoned() {
        CartAbandonedEvent event = new CartAbandonedEvent();
        event.setUserId("test-user-1");
        event.setEmail("test@example.com");
        event.setItems(Collections.emptyList());
        kafkaTemplate.send("cart-abandoned", "cart-abandoned", event);
        return ResponseEntity.ok("Cart abandoned event triggered");
    }

    @PostMapping("/password-changed")
    @Operation(summary = "Trigger password changed event")
    public ResponseEntity<String> triggerPasswordChanged() {
        UserRegisteredEvent event = new UserRegisteredEvent();
        event.setUserId("test-user-1");
        event.setEmail("test@example.com");
        event.setUsername("testuser");
        kafkaTemplate.send("password-changed", "password-changed", event);
        return ResponseEntity.ok("Password changed event triggered");
    }

    @PostMapping("/product-stock-updated")
    @Operation(summary = "Trigger product stock updated event")
    public ResponseEntity<String> triggerProductStockUpdated() {
        ProductStockUpdatedEvent event = new ProductStockUpdatedEvent();
        event.setUserId("test-user-1");
        event.setEmail("test@example.com");
        event.setProductId("test-product-1");
        event.setProductName("Test Product");
        event.setNewStock(100);
        kafkaTemplate.send("product-stock-updated", "product-stock-updated", event);
        return ResponseEntity.ok("Product stock updated event triggered");
    }
} 