package com.example.order.order_service.application.dto;

// CancelOrderCommand.java

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelOrderCommand {
    @NotBlank
    private String initiatorId; // Customer ID or Admin Identifier
    private String reason;
}


