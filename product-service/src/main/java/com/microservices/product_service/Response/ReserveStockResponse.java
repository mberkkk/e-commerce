package com.microservices.product_service.Response;

import com.microservices.product_service.DTO.ReservationInfoDTO;
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
