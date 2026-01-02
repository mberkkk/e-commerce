package com.microservices.product_service.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReserveStockResponse {
    private String orderReference;
    List<ReservationInfoDTO> reservationInfoDTOList;
}
