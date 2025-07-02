package com.microservices.cart_service.Service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// The URL should be configurable and taken from application.properties
@FeignClient(name = "user-service", url = "${services.user.base-url}")
public interface UserServiceClient {

    // A placeholder DTO. A proper response DTO should be defined.
    class UserValidationResponse {
        public boolean valid;
        public String userId;
        public String email;
        public String fullName;
        public boolean canPerformOperations;
        public String validationMessage;
        public boolean active;
    }

    // Assuming user-service will have this internal endpoint
    @GetMapping("/api/internal/users/{userId}/validate")
    UserValidationResponse validateUser(@PathVariable("userId") String userId);
}