// ReserveStockServiceResponse.java
package com.example.order.order_service.infrastructure.persistence.client.feign.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReserveStockServiceResponse {
    private String orderReference;
    private List<ReservationInfoServiceDto> reservationInfoDTOList;
}