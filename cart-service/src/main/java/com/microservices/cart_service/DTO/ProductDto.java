package com.microservices.cart_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price; // Double'dan BigDecimal'e değişti
    private Integer stockQuantity;
    private Boolean isActive;
    private Integer popularityScore;
    private String imageUrl; // Yeni eklendi
    // category field'ını şimdilik eklemeyelim, gerekirse sonra ekleriz
}