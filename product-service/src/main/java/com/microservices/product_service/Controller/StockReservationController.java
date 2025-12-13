package com.microservices.product_service.Controller;

import com.microservices.product_service.Request.ReserveStockRequest;
import com.microservices.product_service.Response.ProductResponse;
import com.microservices.product_service.Response.ReserveStockResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface StockReservationController {

    ReserveStockResponse reserveStock(@RequestBody ReserveStockRequest request);

}
