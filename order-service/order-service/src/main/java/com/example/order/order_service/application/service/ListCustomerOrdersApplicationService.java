package com.example.order.order_service.application.service;

// ListCustomerOrdersService.java (REQ-FUN-003)

import com.example.order.order_service.application.dto.OrderSummaryResponse;
import com.example.order.order_service.application.port.in.ListCustomerOrdersPort;
import com.example.order.order_service.application.port.out.OrderRepositoryPort;
import com.example.order.order_service.domain.model.Order;
import lombok.RequiredArgsConstructor;

//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

//@Service
@RequiredArgsConstructor
public class ListCustomerOrdersApplicationService implements ListCustomerOrdersPort {

    //@Qualifier("orderRepositoryPort")
    private final OrderRepositoryPort orderRepository;

    @Override
    public Page<OrderSummaryResponse> listCustomerOrders(String customerId, Pageable pageable) {
        // REQ-FUN-003: Yetkilendirme kontrolü (customerId zaten sorguya dahil)
        // Admin için farklı bir yetkilendirme mekanizması olabilir.
        Page<Order> ordersPage = orderRepository.findByCustomerId(customerId, pageable);

        return ordersPage.map(order -> OrderSummaryResponse.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .build());
    }
}
