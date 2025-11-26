package com.example.order.order_service.application.port.in;

// PlaceOrderUseCase.java

import com.example.order.order_service.application.dto.CreateOrderCommand;
import com.example.order.order_service.application.dto.OrderDetailsResponse;

public interface CreateOrderPort {
    OrderDetailsResponse createOrder(CreateOrderCommand command);
}
