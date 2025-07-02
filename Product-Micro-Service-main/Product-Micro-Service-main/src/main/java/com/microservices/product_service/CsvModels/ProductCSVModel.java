package com.microservices.product_service.CsvModels;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductCSVModel {
    @CsvBindByName(column = "id")
    private Long id;

    @CsvBindByName(column = "name")
    private String name;

    @CsvBindByName(column = "description")
    private String description;

    @CsvBindByName(column = "price")
    private BigDecimal price;

    @CsvBindByName(column = "stockQuantity")
    private Integer stockQuantity;

    @CsvBindByName(column = "imageUrl")
    private String imageUrl;

    @CsvBindByName(column = "popularityScore")
    private Integer popularityScore;

    @CsvBindByName(column = "categoryCode")
    private Long categoryCode;

    @CsvBindByName(column = "isActive")
    private Boolean isActive;

    @CsvBindByName(column = "createdAt")
    private String createdAt; // Ya da LocalDateTime

    @CsvBindByName(column = "updatedAt")
    private String updatedAt;
}
