package com.microservices.product_service.CsvModels;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockReservationCSVModel {

    @CsvBindByName(column = "id")
    private Long id;

    @CsvBindByName(column = "product_id")
    private Long productId;

    @CsvBindByName(column = "reservedQuantity")
    private Integer reservedQuantity;

    @CsvBindByName(column = "orderReference")
    private String orderReference;

    @CsvBindByName(column = "confirmed")
    private Boolean confirmed;

    @CsvBindByName(column = "createdAt")
    private String createdAt;

    @CsvBindByName(column = "updatedAt")
    private String updatedAt;
}
