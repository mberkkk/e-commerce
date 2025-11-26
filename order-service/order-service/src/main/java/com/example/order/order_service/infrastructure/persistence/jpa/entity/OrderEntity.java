package com.example.order.order_service.infrastructure.persistence.jpa.entity;


import com.example.order.order_service.domain.model.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders") // "order" kelimesi SQL'de keyword olabilir
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
    @Id
    private String orderId;
    private String customerId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderDate;
    private LocalDateTime lastUpdatedDate;
    private BigDecimal totalAmount;
    private String currency;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "order") // <-- BURAYI GÜNCELLEYİN
    private List<OrderItemEntity> items;

    //@Column(name = "payment_method_details")

    private String paymentMethodDetails;
}
