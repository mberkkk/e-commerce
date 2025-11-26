package com.example.order.order_service.domain.exception;

// OrderNotFoundException.java

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super("Order not found with ID: " + orderId);
    }
}

