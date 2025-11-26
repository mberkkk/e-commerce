// ProductServiceDto.java
package com.example.order.order_service.infrastructure.persistence.client.feign.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductServiceDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Integer popularityScore;
    private Boolean isActive;
}