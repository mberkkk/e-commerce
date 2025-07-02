package com.example.UserMicroServiceProject.infrastructure.event;

import com.example.UserMicroServiceProject.infrastructure.client.OrderServiceClient;
import com.example.UserMicroServiceProject.infrastructure.client.ProductServiceClient;
import com.example.UserMicroServiceProject.infrastructure.client.CartServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventListener {

    private final OrderServiceClient orderServiceClient;
    private final ProductServiceClient productServiceClient;
    private final CartServiceClient cartServiceClient;

    @KafkaListener(topics = "ORDER_CREATED", groupId = "${spring.kafka.consumer.group-id}")
    public void handleOrderCreated(String orderId) {
        log.info("Received ORDER_CREATED event for order: {}", orderId);
        // Update user's order history
        orderServiceClient.getOrderDetails(orderId);
    }

    @KafkaListener(topics = "PRODUCT_STOCK_UPDATED", groupId = "${spring.kafka.consumer.group-id}")
    public void handleProductStockUpdated(String productId) {
        log.info("Received PRODUCT_STOCK_UPDATED event for product: {}", productId);
        productServiceClient.getProduct(productId); // Update product cache
    }

    @KafkaListener(topics = "CART_ABANDONED", groupId = "${spring.kafka.consumer.group-id}")
    public void handleCartAbandoned(String userId) {
        log.info("Received CART_ABANDONED event for user: {}", userId);
        // Update user's cart status
        cartServiceClient.getCart(userId);
    }

    @KafkaListener(topics = "ITEM_ADDED_TO_CART", groupId = "${spring.kafka.consumer.group-id}")
    public void handleItemAddedToCart(String userId) {
        log.info("Received ITEM_ADDED_TO_CART event for user: {}", userId);
        // Update user's cart status
        cartServiceClient.getCart(userId);
    }
} 