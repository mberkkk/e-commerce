package com.example.order.order_service.application.dto;

// UpdateOrderStatusCommand.java

import com.example.order.order_service.domain.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusCommand {
    @NotNull
    private OrderStatus newStatus;
    private String notes;
    private String initiatedBy; // Admin ID, Sistem vb.
}


