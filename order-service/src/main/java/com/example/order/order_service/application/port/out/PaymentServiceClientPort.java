package com.example.order.order_service.application.port.out;


// PaymentServicePort.java (Mocked internally)

import java.math.BigDecimal;

public interface PaymentServiceClientPort {
    boolean authorizePayment(String customerId, BigDecimal amount, String paymentToken);
    boolean capturePayment(String orderId, BigDecimal amount);
    boolean refundPayment(String orderId, BigDecimal amount);
}


