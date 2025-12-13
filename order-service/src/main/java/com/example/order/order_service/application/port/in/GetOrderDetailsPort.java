package com.example.order.order_service.application.port.in;

// GetOrderDetailsUseCase.java

import com.example.order.order_service.application.dto.OrderDetailsResponse;

public interface GetOrderDetailsPort {
    OrderDetailsResponse getOrderDetails(String orderId, String customerId); // customerId auth için
}


