package com.example.order.order_service.application.dto;

// OrderDetailsResponse.java

import com.example.order.order_service.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDetailsResponse {
    private String orderId;
    private String customerId;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime lastUpdatedDate;
    private BigDecimal totalAmount;
    private String currency;
    private List<OrderItemDto> items;
}

