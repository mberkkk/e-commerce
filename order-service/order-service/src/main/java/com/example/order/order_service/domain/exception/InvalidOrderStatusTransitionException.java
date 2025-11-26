package com.example.order.order_service.domain.exception;

// InvalidOrderStatusTransitionException.java
public class InvalidOrderStatusTransitionException extends RuntimeException {
    public InvalidOrderStatusTransitionException(String message) {
        super(message);
    }
}


