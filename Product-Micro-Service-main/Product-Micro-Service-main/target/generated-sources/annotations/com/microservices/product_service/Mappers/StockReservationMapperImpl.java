package com.microservices.product_service.Mappers;

import com.microservices.product_service.DTO.StockReservationDTO;
import com.microservices.product_service.Entity.StockReservation;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class StockReservationMapperImpl implements StockReservationMapper {

    @Override
    public StockReservation toEntity(StockReservationDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        StockReservation.StockReservationBuilder stockReservation = StockReservation.builder();

        stockReservation.id( productDTO.getId() );
        stockReservation.product( productDTO.getProduct() );
        stockReservation.reservedQuantity( productDTO.getReservedQuantity() );
        stockReservation.orderReference( productDTO.getOrderReference() );
        stockReservation.confirmed( productDTO.getConfirmed() );

        return stockReservation.build();
    }

    @Override
    public StockReservationDTO toDTO(StockReservation stockReservation) {
        if ( stockReservation == null ) {
            return null;
        }

        StockReservationDTO stockReservationDTO = new StockReservationDTO();

        stockReservationDTO.setId( stockReservation.getId() );
        stockReservationDTO.setProduct( stockReservation.getProduct() );
        stockReservationDTO.setReservedQuantity( stockReservation.getReservedQuantity() );
        stockReservationDTO.setOrderReference( stockReservation.getOrderReference() );
        stockReservationDTO.setConfirmed( stockReservation.getConfirmed() );

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
