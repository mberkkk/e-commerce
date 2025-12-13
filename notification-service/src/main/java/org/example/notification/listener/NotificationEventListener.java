package org.example.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notification.dto.event.CartAbandonedEvent;
import org.example.notification.dto.event.ItemAddedToCartEvent;
import org.example.notification.dto.event.OrderCreatedEvent;
import org.example.notification.dto.event.ProductStockUpdatedEvent;
import org.example.notification.dto.event.UserRegisteredEvent;
import org.example.notification.service.EmailService;
import org.example.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.example.notification.dto.NotificationRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final EmailService emailService;
    private final NotificationService notificationService;

    @KafkaListener(topics = "user-registered", groupId = "notification-service")
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Received user registered event: {}", event);
        try {
            // Create notification first
            NotificationRequest request = NotificationRequest.builder()
                    .userId(event.getUserId())
                    .type("USER_REGISTERED")
                    .message("Welcome to our platform, " + event.getUsername() + "!")
                    .channel("EMAIL")
                    .build();

            notificationService.createNotification(request);
            log.info("Created notification for user registration: {}", request);

            // Try to send email, but don't fail if it doesn't work
            try {
                emailService.sendWelcomeEmail(event.getEmail(), event.getUsername());
                log.info("Welcome email sent successfully to: {}", event.getEmail());
            } catch (Exception e) {
                log.error("Failed to send welcome email: {}", e.getMessage());
                // Don't rethrow the exception
            }
        } catch (Exception e) {
            log.error("Error processing user registered event: {}", e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "order-created", groupId = "notification-service")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received order created event: {}", event);
        try {
            // Create notification first
            NotificationRequest request = NotificationRequest.builder()
                    .userId(event.getUserId())
                    .type("ORDER_CREATED")
                    .message("Your order " + event.getOrderId() + " has been created successfully! Total amount: "
                            + event.getTotalAmount())
                    .channel("EMAIL")
                    .build();

            notificationService.createNotification(request);
            log.info("Created notification for order creation: {}", request);

            // Try to send email, but don't fail if it doesn't work
            try {
                emailService.sendOrderConfirmationEmail(event.getEmail(), event.getOrderId(),
                        event.getTotalAmount().toString());
                log.info("Order confirmation email sent successfully to: {}", event.getEmail());
            } catch (Exception e) {
                log.error("Failed to send order confirmation email: {}", e.getMessage());
                // Don't rethrow the exception
            }
        } catch (Exception e) {
            log.error("Error processing order created event: {}", e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "cart-abandoned", groupId = "notification-service")
    public void handleCartAbandoned(CartAbandonedEvent event) {
        log.info("Received cart abandoned event: {}", event);
        try {
            // Create notification first
            NotificationRequest request = NotificationRequest.builder()
                    .userId(event.getUserId())
                    .type("CART_ABANDONED")
                    .message("Don't forget your cart items! Complete your purchase to get the best deals.")
                    .channel("EMAIL")
                    .build();

            notificationService.createNotification(request);
            log.info("Created notification for cart abandoned: {}", request);

            // Try to send email, but don't fail if it doesn't work
            try {
                emailService.sendCartAbandonedEmail(event.getEmail(), event.getUserId());
                log.info("Cart abandoned email sent successfully to: {}", event.getEmail());
            } catch (Exception e) {
                log.error("Failed to send cart abandoned email: {}", e.getMessage());
                // Don't rethrow the exception
            }
        } catch (Exception e) {
            log.error("Error processing cart abandoned event: {}", e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "password-changed", groupId = "notification-service")
    public void handlePasswordChanged(UserRegisteredEvent event) {
        log.info("Received password changed event: {}", event);
        try {
            // Create notification first
            NotificationRequest request = NotificationRequest.builder()
                    .userId(event.getUserId())
                    .type("PASSWORD_CHANGED")
                    .message(
                            "Your password has been changed successfully. If you didn't make this change, please contact support.")
                    .channel("EMAIL")
                    .build();

            notificationService.createNotification(request);
            log.info("Created notification for password change: {}", request);

            // Try to send email, but don't fail if it doesn't work
            try {
                emailService.sendPasswordChangedEmail(event.getEmail(), event.getUsername());
                log.info("Password changed email sent successfully to: {}", event.getEmail());
            } catch (Exception e) {
                log.error("Failed to send password changed email: {}", e.getMessage());
                // Don't rethrow the exception
            }
        } catch (Exception e) {
            log.error("Error processing password changed event: {}", e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "item-added-to-cart", groupId = "notification-service")
    public void handleItemAddedToCart(ItemAddedToCartEvent event) {
        log.info("Received item added to cart event: {}", event);
        try {
            // Create notification first
            NotificationRequest request = NotificationRequest.builder()
                    .userId(event.getUserId())
                    .type("ITEM_ADDED_TO_CART")
                    .message("Item added to your cart! Don't forget to complete your purchase.")
                    .channel("EMAIL")
                    .build();

            notificationService.createNotification(request);
            log.info("Created notification for item added to cart: {}", request);

            // Try to send email, but don't fail if it doesn't work
            try {
                emailService.sendCartReminderEmail(event.getUserId(), event.getProductId().toString());
                log.info("Cart reminder email sent successfully to user: {}", event.getUserId());
            } catch (Exception e) {
                log.error("Failed to send cart reminder email: {}", e.getMessage());
                // Don't rethrow the exception
            }
        } catch (Exception e) {
            log.error("Error processing item added to cart event: {}", e.getMessage(), e);
            throw e;
        }
    }

    @KafkaListener(topics = "product-stock-updated", groupId = "notification-service")
    public void handleProductStockUpdated(ProductStockUpdatedEvent event) {
        log.info("Received product stock updated event: {}", event);
        try {
            // Create notification first
            NotificationRequest request = NotificationRequest.builder()
                    .userId(event.getUserId())
                    .type("STOCK_UPDATED")
                    .message("Product " + event.getProductName() + " is now back in stock with " + event.getNewStock()
                            + " items available!")
                    .channel("EMAIL")
                    .build();

            notificationService.createNotification(request);
            log.info("Created notification for stock update: {}", request);

            // Try to send email, but don't fail if it doesn't work
            try {
                emailService.sendStockUpdateEmail(event.getEmail(), event.getProductName());
                log.info("Stock update email sent successfully to: {}", event.getEmail());
            } catch (Exception e) {
                log.error("Failed to send stock update email: {}", e.getMessage());
                // Don't rethrow the exception
            }
        } catch (Exception e) {
            log.error("Error processing product stock updated event: {}", e.getMessage(), e);
            throw e;
        }
    }
}