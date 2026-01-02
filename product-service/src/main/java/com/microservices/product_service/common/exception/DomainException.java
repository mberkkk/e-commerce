package com.microservices.product_service.common.exception;

public class DomainException extends IllegalArgumentException{
    public DomainException(String message) {
        super(message);
    }
}
