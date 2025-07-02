package com.microservices.product_service.CsvModels;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCSVModel {

    @CsvBindByName(column = "id")
    private Long id;

    @CsvBindByName(column = "categoryCode")
    private Long categoryCode;

    @CsvBindByName(column = "categoryType")
    private String categoryType;

    @CsvBindByName(column = "createdAt")
    private String createdAt;

    @CsvBindByName(column = "updatedAt")
    private String updatedAt;
}
