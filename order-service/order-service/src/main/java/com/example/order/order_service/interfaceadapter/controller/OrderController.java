package com.example.order.order_service.interfaceadapter.controller;

// OrderController.java (REQ-INT-001)

import com.example.order.order_service.application.dto.*;
import com.example.order.order_service.application.port.in.*;
import com.example.order.order_service.application.service.PaymentEventHandlerApplicationService; // Sadece test amaçlı simülasyon için
import com.example.order.order_service.domain.exception.InvalidOrderStatusTransitionException;
import com.example.order.order_service.domain.exception.OrderNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.order.order_service.infrastructure.persistence.event.consumer.PaymentEventKafkaConsumer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderPort createOrderPort;
    private final GetOrderDetailsPort getOrderDetailsPort;
    private final ListCustomerOrdersPort listCustomerOrdersPort;
    private final UpdateOrderStatusPort updateOrderStatusPort;
    private final CancelOrderPort cancelOrderPort;
    // private final PaymentEventHandlerApplicationService
    // paymentEventHandlerService; // Sadece test amaçlı simülasyon için
    private final PaymentEventKafkaConsumer paymentEventKafkaConsumer; // YENİ ENJEKSİYON

    // REQ-FUN-001: Place New Order
    @PostMapping
    public ResponseEntity<OrderDetailsResponse> placeOrder(@Valid @RequestBody CreateOrderRequest request) {
        // REQ-NON-006: Güvenlik - Customer ID'yi JWT token'dan almalısın
        // Şimdilik request body'den alınıyor.
        // String authenticatedUserId = getUserIdFromAuthToken(); // TODO

        CreateOrderCommand command = new CreateOrderCommand();
        command.setUserId(request.getUserId());
        command.setShippingAddress(request.getShippingAddress());

        OrderDetailsResponse response = createOrderPort.createOrder(command);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // REQ-FUN-002: Get Order Details
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable String orderId) {
        // REQ-NON-006: Yetkilendirme - Sadece ilgili müşteri veya admin görebilir
        String authenticatedCustomerId = "customer-postman-001"; // <-- BURAYI DEĞİŞTİRİN
        // Admin yetkisi varsa customerId kontrolü yapılmaz.
        OrderDetailsResponse response = getOrderDetailsPort.getOrderDetails(orderId, authenticatedCustomerId);
        return ResponseEntity.ok(response);
    }

    // Diğer yetkilendirme kontrolü olan metotlar (List Customer Orders, Cancel
    // Order) için de aynı değişiklikleri yapmanız gerekebilir:
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Page<OrderSummaryResponse>> listCustomerOrders(
            @PathVariable String customerId,
            Pageable pageable) {
        String authenticatedCustomerId = "customer-postman-001"; // <-- BURAYI DEĞİŞTİRİN
        if (!customerId.equals(authenticatedCustomerId) /* && !hasAdminRole() */) {
            throw new SecurityException("Access denied: You can only view your own orders.");
        }
        Page<OrderSummaryResponse> response = listCustomerOrdersPort.listCustomerOrders(customerId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDetailsResponse> cancelOrder(
            @PathVariable String orderId,
            @RequestBody(required = false) CancelOrderCommand command) {
        String initiatorId = "customer-postman-001"; // <-- BURAYI DEĞİŞTİRİN
        if (command == null) {
            command = new CancelOrderCommand();
        }
        command.setInitiatorId(initiatorId);

        OrderDetailsResponse response = cancelOrderPort.cancelOrder(orderId, command);
        return ResponseEntity.ok(response);
    }

    // REQ-FUN-004: Update Order Status (Admin/Sistem tarafından)
    @PatchMapping("/{orderId}/status") // <-- BU ANOTASYON TAM OLARAK BÖYLE Mİ?
    public ResponseEntity<OrderDetailsResponse> updateOrderStatus(
            @PathVariable String orderId,
            @Valid @RequestBody UpdateOrderStatusCommand command) {
        // REQ-NON-006: Yetkilendirme - Sadece admin veya sistem tarafından çağrılabilir
        command.setInitiatedBy("admin-456"); // Mock

        OrderDetailsResponse response = updateOrderStatusPort.updateOrderStatus(orderId, command);
        return ResponseEntity.ok(response);
    }

    // REQ-FUN-006 Simülasyon Endpointi (Gerçekte bu, bir Kafka Consumer tarafından
    // tetiklenir)
    // @PostMapping("/simulate-payment-event")
    // public ResponseEntity<String> simulatePaymentEvent(@RequestBody PaymentEvent
    // event) {
    // Bu endpoint sadece test ve demo amaçlıdır.
    // Gerçekte ödeme servisi, event'i Kafka'ya yazar ve bu servis onu consumer ile
    // dinler.
    // paymentEventHandlerService.simulatePaymentEvent(event);
    // return ResponseEntity.ok("Payment event simulated successfully.");
    // }

    // REQ-FUN-006 Simülasyon Endpointi (Gerçekte bu, bir Kafka Consumer tarafından
    // tetiklenir)
    @PostMapping("/simulate-payment-event")
    public ResponseEntity<String> simulatePaymentEvent(@RequestBody PaymentEvent event) {
        // Bu endpoint sadece test ve demo amaçlıdır.
        // Gerçekte ödeme servisi, event'i Kafka'ya yazar ve bu servis onu consumer ile
        // dinler.
        paymentEventKafkaConsumer.simulatePaymentEvent(event); // Burası değişti
        return ResponseEntity.ok("Payment event simulated successfully.");
    }
}
