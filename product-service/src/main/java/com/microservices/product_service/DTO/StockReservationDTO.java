package com.microservices.product_service.DTO;

import com.microservices.product_service.Entity.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StockReservationDTO extends BaseDTO{
    private Long id;
    private Product product;
    private Integer reservedQuantity;
    private String orderReference; // orderId gibi referans
    private Boolean confirmed;

}
