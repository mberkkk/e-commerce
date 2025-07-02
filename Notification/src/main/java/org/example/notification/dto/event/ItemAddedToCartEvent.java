package org.example.notification.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAddedToCartEvent {
    private String userId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime timestamp;
}