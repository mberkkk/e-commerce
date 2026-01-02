package com.microservices.product_service.stock.dto;

import lombok.Data;

@Data
public class ReservationInfoDTO {
    private Long stockReservationId;
    private Long productid;
    private String message;
    private boolean outOfStockFlag;
    private boolean stockReservedFlag;
}
