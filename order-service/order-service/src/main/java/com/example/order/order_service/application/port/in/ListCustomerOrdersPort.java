package com.example.order.order_service.application.port.in;

// ListCustomerOrdersUseCase.java

import com.example.order.order_service.application.dto.OrderSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListCustomerOrdersPort {
    Page<OrderSummaryResponse> listCustomerOrders(String customerId, Pageable pageable);
}


