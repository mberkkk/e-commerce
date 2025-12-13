// ReservationInfoServiceDto.java
package com.example.order.order_service.infrastructure.persistence.client.feign.dto;

import lombok.Data;

@Data
public class ReservationInfoServiceDto {
    private Long productid;
    private boolean outOfStockFlag;
    private boolean stockReservedFlag;
    private String message;
    private Long stockReservationId;
}