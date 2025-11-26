package com.example.order.order_service.application.port.in;

// CancelOrderUseCase.java

import com.example.order.order_service.application.dto.OrderDetailsResponse;
import com.example.order.order_service.application.dto.CancelOrderCommand;

public interface CancelOrderPort {
    OrderDetailsResponse cancelOrder(String orderId, CancelOrderCommand command);
}
