package com.example.order.order_service.application.port.in;

// UpdateOrderStatusUseCase.java

import com.example.order.order_service.application.dto.OrderDetailsResponse;
import com.example.order.order_service.application.dto.UpdateOrderStatusCommand;

public interface UpdateOrderStatusPort {
    OrderDetailsResponse updateOrderStatus(String orderId, UpdateOrderStatusCommand command);
}


