package com.example.order.order_service.application.port.in;

// HandlePaymentEventUseCase.java (REQ-FUN-006)

import com.example.order.order_service.application.dto.PaymentEvent;

public interface HandlePaymentEventPort {
    void handlePaymentEvent(PaymentEvent event);
}
