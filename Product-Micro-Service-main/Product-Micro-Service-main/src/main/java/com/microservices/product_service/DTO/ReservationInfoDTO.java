package com.microservices.product_service.DTO;

import lombok.Data;

@Data
public class ReservationInfoDTO {
    private Long stockReservationId;
    private Long productid;
    private String message;
    private boolean outOfStockFlag;
    private boolean stockReservedFlag;
}
