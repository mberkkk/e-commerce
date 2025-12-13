package com.example.order.order_service.infrastructure.persistence.client;

import com.example.order.order_service.application.port.out.PaymentServiceClientPort;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Random;

@Component // Bu kalacak
public class PaymentServiceMockClientAdapter implements PaymentServiceClientPort {

    // private final Random random = new Random(); // Artık Random'a ihtiyacımız yok

    @Override
    public boolean authorizePayment(String customerId, BigDecimal amount, String paymentToken) {
        System.out.println("Mock PaymentService: Authorizing payment for customer " + customerId + ", amount " + amount + " with token " + paymentToken);
        // Simulate payment authorization success/failure
        // return random.nextBoolean(); // Eski rastgele davranış
        return true; // <-- BURAYI DEĞİŞTİRİN: Her zaman başarılı olsun
    }

    @Override
    public boolean capturePayment(String orderId, BigDecimal amount) {
        System.out.println("Mock PaymentService: Capturing payment for order " + orderId + ", amount " + amount);
        return true;
    }

    @Override
    public boolean refundPayment(String orderId, BigDecimal amount) {
        System.out.println("Mock PaymentService: Refunding payment for order " + orderId + ", amount " + amount);
        return true;
    }
}