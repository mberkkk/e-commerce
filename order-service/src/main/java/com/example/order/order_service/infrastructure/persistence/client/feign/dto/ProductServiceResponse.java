// ProductServiceResponse.java
package com.example.order.order_service.infrastructure.persistence.client.feign.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductServiceResponse {
    private ProductServiceDto productDTO;
}