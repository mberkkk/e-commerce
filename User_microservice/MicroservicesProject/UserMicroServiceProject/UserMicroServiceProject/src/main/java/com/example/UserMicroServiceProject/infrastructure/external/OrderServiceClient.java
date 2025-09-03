package com.example.UserMicroServiceProject.infrastructure.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service", url = "${external-services.order-service.url}", path = "/api/orders")
public interface OrderServiceClient {

    @GetMapping("/validate-user/{userId}")
    boolean validateUser(@PathVariable("userId") String userId);

    @GetMapping("/{orderId}")
    OrderDTO getOrderDetails(@PathVariable("orderId") String orderId);

    @GetMapping("/user/{userId}")
    java.util.List<OrderDTO> getUserOrders(@PathVariable("userId") String userId);

    @PostMapping("/clear-cart/{userId}")
    void clearCart(@PathVariable("userId") String userId);

    record OrderDTO(
            String id,
            String userId,
            String status,
            double totalAmount,
            java.util.List<OrderItemDTO> items,
            AddressDTO shippingAddress,
            java.time.LocalDateTime createdAt
    ) {}

    record OrderItemDTO(
            String productId,
            String productName,
            int quantity,
            double price
    ) {}

    record AddressDTO(
            String id,
            String userId,
            String street,
            String city,
            String state,
            String country,
            String zipCode,
            boolean isDefault
    ) {}
}