package com.microservices.product_service.DTO.External;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventItemDTO {
    private String productId; // Order service String gönderiyor!
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;
}