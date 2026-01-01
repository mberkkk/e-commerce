package com.microservices.product_service.DTO;

import com.microservices.product_service.Entity.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StockReservationDTO extends BaseDTO{
    private Long id;
    private Product product;
    private Integer reservedQuantity;
    private String orderReference;
    private Boolean confirmed;

}
