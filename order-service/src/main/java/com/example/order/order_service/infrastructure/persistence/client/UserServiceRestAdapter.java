package com.example.order.order_service.infrastructure.persistence.client;

import com.example.order.order_service.application.port.out.UserServiceClientPort;
import org.springframework.stereotype.Component;

@Component
public class UserServiceRestAdapter implements UserServiceClientPort {

    // TODO: Inject RestTemplate or FeignClient

    @Override
    public boolean validateUser(String userId) {
        // Mock implementation
        System.out.println("Simulating user validation for userId: " + userId);
        if (userId == null || userId.isBlank()) {
            return false;
        }
        // In a real scenario, this would make an HTTP call to the User Service.
        // For now, we'll assume any non-blank user ID is valid.
        return true;
    }
}