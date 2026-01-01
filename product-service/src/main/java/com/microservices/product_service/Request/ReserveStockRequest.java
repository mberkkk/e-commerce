package com.microservices.product_service.Request;

import com.microservices.product_service.DTO.StockProductInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ReserveStockRequest {
    private String orderReference;
    private List<StockProductInfoDTO> stockProductInfoDTOS;
}
