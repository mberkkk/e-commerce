package com.microservices.product_service.Event;

import com.microservices.product_service.Response.ReserveStockResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCreatedEvent {
    private final ReserveStockResponse response;
}