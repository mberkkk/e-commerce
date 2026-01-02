package com.microservices.product_service.stock.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReserveStockRequest {
    private String orderReference;
    private List<StockProductInfoDTO> stockProductInfoDTOS;
}
