package com.microservices.product_service.Response;

import com.microservices.product_service.stock.dto.ReserveStockResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private String id;
    private String status;
    private String message;
    private ReserveStockResponse payload;
}
