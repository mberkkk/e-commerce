// StockProductInfoServiceDto.java
package com.example.order.order_service.infrastructure.persistence.client.feign.dto;

import lombok.Data;

@Data
public class StockProductInfoServiceDto {
    private Long productId;
    private Integer wantedQuantity;
}