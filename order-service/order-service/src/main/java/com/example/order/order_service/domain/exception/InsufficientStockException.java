package com.example.order.order_service.domain.exception;

// InsufficientStockException.java

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productId) {
        super("Insufficient stock for product ID: " + productId);
    }
}
