package com.microservices.product_service.DTO.External;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private String orderId;
    private String userId;
    private List<OrderEventItemDTO> items; // İsmi değiştirdim, aşağıya bak
    private BigDecimal totalAmount;
    private Instant timestamp;
}