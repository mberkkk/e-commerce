package com.microservices.product_service.Mappers;

import com.microservices.product_service.DTO.StockReservationDTO;
import com.microservices.product_service.Entity.StockReservation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class StockReservationMapperImpl implements StockReservationMapper {

    @Override
    public StockReservation toEntity(StockReservationDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        StockReservation.StockReservationBuilder stockReservation = StockReservation.builder();

        stockReservation.confirmed( productDTO.getConfirmed() );
        stockReservation.id( productDTO.getId() );
        stockReservation.orderReference( productDTO.getOrderReference() );
        stockReservation.product( productDTO.getProduct() );
        stockReservation.reservedQuantity( productDTO.getReservedQuantity() );

        return stockReservation.build();
    }

    @Override
    public StockReservationDTO toDTO(StockReservation stockReservation) {
        if ( stockReservation == null ) {
            return null;
        }

        StockReservationDTO stockReservationDTO = new StockReservationDTO();

        stockReservationDTO.setConfirmed( stockReservation.getConfirmed() );
        stockReservationDTO.setId( stockReservation.getId() );
        stockReservationDTO.setOrderReference( stockReservation.getOrderReference() );
        stockReservationDTO.setProduct( stockReservation.getProduct() );
        stockReservationDTO.setReservedQuantity( stockReservation.getReservedQuantity() );

        return stockReservationDTO;
    }

    @Override
    public List<StockReservation> toEntityList(List<StockReservationDTO> productDTOS) {
        if ( productDTOS == null ) {
            return null;
        }

        List<StockReservation> list = new ArrayList<StockReservation>( productDTOS.size() );
        for ( StockReservationDTO stockReservationDTO : productDTOS ) {
            list.add( toEntity( stockReservationDTO ) );
        }

        return list;
    }

    @Override
    public List<StockReservationDTO> toDTOList(List<StockReservation> stockReservations) {
        if ( stockReservations == null ) {
            return null;
        }

        List<StockReservationDTO> list = new ArrayList<StockReservationDTO>( stockReservations.size() );
        for ( StockReservation stockReservation : stockReservations ) {
            list.add( toDTO( stockReservation ) );
        }

        return list;
    }
}
