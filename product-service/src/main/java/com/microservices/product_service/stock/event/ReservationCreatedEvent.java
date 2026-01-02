package com.microservices.product_service.stock.event;

import com.microservices.product_service.stock.dto.ReserveStockResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCreatedEvent {
    private final ReserveStockResponse response;
}