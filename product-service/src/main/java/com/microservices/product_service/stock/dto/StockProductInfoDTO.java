package com.microservices.product_service.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockProductInfoDTO {
    private Long productId;
    private Integer wantedQuantity;
}
