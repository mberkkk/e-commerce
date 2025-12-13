package com.example.order.order_service.application.dto;

// OrderSummaryResponse.java

import com.example.order.order_service.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderSummaryResponse {
    private String orderId;
    private String customerId;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String currency;
}

