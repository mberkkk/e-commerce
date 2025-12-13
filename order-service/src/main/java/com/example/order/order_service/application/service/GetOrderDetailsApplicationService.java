package com.example.order.order_service.application.service;

// GetOrderDetailsService.java (REQ-FUN-002)

import com.example.order.order_service.application.dto.OrderDetailsResponse;
import com.example.order.order_service.application.dto.OrderItemDto;
import com.example.order.order_service.application.port.in.GetOrderDetailsPort;
import com.example.order.order_service.application.port.out.OrderRepositoryPort;
import com.example.order.order_service.domain.exception.OrderNotFoundException;
import com.example.order.order_service.domain.model.Order;
import lombok.RequiredArgsConstructor;

//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

//@Service
@RequiredArgsConstructor
public class GetOrderDetailsApplicationService implements GetOrderDetailsPort {

    //@Qualifier("orderRepositoryPort")
    private final OrderRepositoryPort orderRepository;

    @Override
    public OrderDetailsResponse getOrderDetails(String orderId, String customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // REQ-FUN-002: Yetkilendirme kontrolü (Müşteri sadece kendi siparişini görebilir)
        if (!order.getCustomerId().equals(customerId)) {
            // Admin için farklı bir yetkilendirme mekanizması olabilir
            throw new SecurityException("Access denied: Customer " + customerId + " cannot view order " + orderId);
        }

        return mapToOrderDetailsResponse(order);
    }

    private OrderDetailsResponse mapToOrderDetailsResponse(Order order) {
        return OrderDetailsResponse.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .lastUpdatedDate(order.getLastUpdatedDate())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .items(order.getItems().stream()
                        .map(item -> OrderItemDto.builder()
                                .productId(item.getProductId())
                                .productName(item.getCapturedProductName())
                                .unitPrice(item.getCapturedUnitPrice())
                                .quantity(item.getQuantity())
                                .lineItemTotal(item.getLineItemTotal())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
