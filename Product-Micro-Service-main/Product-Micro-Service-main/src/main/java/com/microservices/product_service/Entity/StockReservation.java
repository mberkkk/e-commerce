package com.microservices.product_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

@Table(name = "STOCK_RESERVATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class StockReservation extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id") // foreign key sütunu
    private Product product;
    private Integer reservedQuantity;
    private String orderReference; // orderId gibi referans
    private Boolean confirmed;

}
