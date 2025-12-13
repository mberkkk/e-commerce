package com.microservices.product_service.Service;

import com.microservices.product_service.Entity.StockReservation;
import com.microservices.product_service.Request.ReserveStockRequest;
import com.microservices.product_service.Response.ProductResponse;
import com.microservices.product_service.Response.ReserveStockResponse;

public interface StockReservationService {

    ReserveStockResponse reserveStock(ReserveStockRequest request);

    boolean setReservedStockComplete(Long id);

    StockReservation findById(Long id);
}
