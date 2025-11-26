package com.example.order.order_service.application.dto;

// PaymentEvent.java (REQ-FUN-006)

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {
    public enum PaymentEventType {
        PAYMENT_CAPTURED,
        PAYMENT_AUTHORIZATION_FAILED,
        PAYMENT_REFUNDED,
        PAYMENT_FAILED_GENERIC
    }

    private String eventId;
    private PaymentEventType type;
    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime timestamp;
    private String transactionId;
    private String statusDetails;
}


