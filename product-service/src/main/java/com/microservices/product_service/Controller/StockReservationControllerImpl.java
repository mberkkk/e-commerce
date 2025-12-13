package com.microservices.product_service.Controller;

import com.microservices.product_service.Request.ReserveStockRequest;
import com.microservices.product_service.Response.ReserveStockResponse;
import com.microservices.product_service.Service.StockReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/products")
@RestController
@RequiredArgsConstructor
@Slf4j
public class StockReservationControllerImpl implements StockReservationController {
    private final StockReservationService stockReservationService;

    @Override
    @PostMapping("/reserve-stock") // GET değil POST olmalı
    public ReserveStockResponse reserveStock(@RequestBody ReserveStockRequest request) {
        log.info("Stock reservation request received for order: {}", request.getOrderReference());
        return stockReservationService.reserveStock(request);
    }

    // Health check endpoint'i de ekleyelim
    @GetMapping("/reserve-stock")
    public String checkReservationEndpoint() {
        return "Stock reservation endpoint is active";
    }
}