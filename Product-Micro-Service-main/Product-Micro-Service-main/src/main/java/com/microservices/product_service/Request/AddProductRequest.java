package com.microservices.product_service.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    @Schema(description = "currency")
    private String currency;
    private Integer stockQuantity;
    private String imageUrl;
    private Integer popularityScore;
    private Boolean isActive;
    private Long categoryCode;
    private String categoryType;
}
