package com.microservices.product_service.Mappers;

import com.microservices.product_service.DTO.ProductDTO;
import com.microservices.product_service.DTO.StockReservationDTO;
import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Entity.StockReservation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockReservationMapper {
    StockReservation toEntity(StockReservationDTO productDTO);

    StockReservationDTO toDTO(StockReservation stockReservation);

    List<StockReservation> toEntityList(List<StockReservationDTO> productDTOS);

    List<StockReservationDTO> toDTOList(List<StockReservation> stockReservations);
}
