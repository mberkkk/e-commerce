package com.microservices.product_service.Service;

import com.microservices.product_service.DTO.ReservationInfoDTO;
import com.microservices.product_service.Entity.StockReservation;
import com.microservices.product_service.Request.AddProductRequest;
import com.microservices.product_service.Response.EventResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationConsumer {
    private final ProductService productService;
    private final StockReservationService stockReservationService;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(EventResponse event) {
        log.info("Order confirmation message received");

        List<ReservationInfoDTO> allResponses = event.getPayload().getReservationInfoDTOList();

        for (ReservationInfoDTO reservationInfoDTO : allResponses) {
            if (reservationInfoDTO.getStockReservationId() != null) {
                try {
                    // Rezervasyonu onayla
                    stockReservationService.setReservedStockComplete(reservationInfoDTO.getStockReservationId());

                    // Stock'ı kalıcı olarak azalt
                    StockReservation reservation = stockReservationService
                            .findById(reservationInfoDTO.getStockReservationId());

                    productService.findById(reservationInfoDTO.getProductid()).ifPresent(product -> {
                        AddProductRequest addProductRequest = new AddProductRequest();
                        int newStock = product.getStockQuantity() - reservation.getReservedQuantity();
                        addProductRequest.setStockQuantity(newStock);

                        productService.updateStock(reservationInfoDTO.getProductid(), addProductRequest);

                        // ✅ Popularity score güncelle
                        productService.updatePopularityScore(reservationInfoDTO.getProductid(),
                                reservation.getReservedQuantity());

                        log.info("Stock updated for product {}: new stock = {}, popularity increased by {}",
                                reservationInfoDTO.getProductid(), newStock, reservation.getReservedQuantity());
                    });

                } catch (Exception e) {
                    log.error("Error processing reservation {}: {}",
                            reservationInfoDTO.getStockReservationId(), e.getMessage());
                }
            }
        }

        event.setStatus("DONE");
        log.info("Order confirmation processing completed");
    }
}