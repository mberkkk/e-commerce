package com.microservices.product_service.Service;

import com.microservices.product_service.DTO.ReservationInfoDTO;
import com.microservices.product_service.DTO.StockProductInfoDTO;
import com.microservices.product_service.Entity.Product;
import com.microservices.product_service.Entity.StockReservation;
import com.microservices.product_service.Mappers.StockReservationMapper;
import com.microservices.product_service.Repository.StockReservationRepository;
import com.microservices.product_service.Request.ReserveStockRequest;
import com.microservices.product_service.Response.EventResponse;
import com.microservices.product_service.Response.ReserveStockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockReservationServiceImpl implements StockReservationService{
    private final ProductService productService;
    private final StockReservationRepository repository;
    private final ReservationProducer producer;
    private final StockReservationMapper mapper;

    @Override
    public ReserveStockResponse reserveStock(ReserveStockRequest request) {
        ReserveStockResponse response = new ReserveStockResponse();
        response.setOrderReference(request.getOrderReference());
        List<ReservationInfoDTO> reservationInfoDTOList = new ArrayList<>();

        for (StockProductInfoDTO stockProductInfoDTO : request.getStockProductInfoDTOS()) {
            ReservationInfoDTO reservationInfoDTO = new ReservationInfoDTO();
            Optional<Product> productOpt = productService.findById(stockProductInfoDTO.getProductId());
            if (productOpt.isEmpty()) {
                reservationInfoDTO.setProductid(stockProductInfoDTO.getProductId());
                reservationInfoDTO.setOutOfStockFlag(true);
                reservationInfoDTO.setStockReservedFlag(false);
                reservationInfoDTO.setMessage("Ürün bulunamadı: ID = " + stockProductInfoDTO.getProductId());
                reservationInfoDTOList.add(reservationInfoDTO);
                continue;
            }

            Product product = productOpt.get();
            if (!validateStock(product, stockProductInfoDTO.getWantedQuantity())) {
                reservationInfoDTO.setProductid(product.getId());
                reservationInfoDTO.setOutOfStockFlag(true);
                reservationInfoDTO.setStockReservedFlag(false);
                reservationInfoDTO.setMessage(product.getId() + " id'li ürün için yeterli stok yok. Mevcut stok: " + product.getStockQuantity());
                reservationInfoDTOList.add(reservationInfoDTO);
                continue;
            }

            reservationInfoDTO.setProductid(product.getId());
            reservationInfoDTO.setOutOfStockFlag(false);
            reservationInfoDTO.setStockReservedFlag(true);
            reservationInfoDTO.setMessage(product.getId() + " id'li ürün için stok ayirtilmistir.");
            reservationInfoDTOList.add(reservationInfoDTO);

            StockReservation stockReservation = StockReservation.builder()
                    .product(product)
                    .confirmed(false)
                    .reservedQuantity(stockProductInfoDTO.getWantedQuantity())
                    .orderReference(request.getOrderReference())
                    .build();
            StockReservation updatedReservation = repository.save(stockReservation);
            reservationInfoDTO.setStockReservationId(updatedReservation.getId());

        }
        response.setReservationInfoDTOList(reservationInfoDTOList);
        producer.sendMessage(EventResponse.builder()
                .status("PENDING")
                .payload(response)
                .build());
        return response;
    }

    @Override
    public boolean setReservedStockComplete(Long id) {
        Optional<StockReservation> reservationOpt = repository.findById(id);

        if(reservationOpt.isPresent()) {
            StockReservation reservation = reservationOpt.get();
            reservation.setConfirmed(true);
            repository.save(reservation);
            return true;
        }
        return false;
    }

    @Override
    public StockReservation findById(Long id) {
        Optional<StockReservation> reservation = repository.findById(id);
        if(reservation.isPresent()) {
            return reservation.get();
        }
        return null;
    }


    private boolean validateStock(Product product, Integer wantedQuantity) {
        return product.getStockQuantity() >= wantedQuantity;
    }
}
