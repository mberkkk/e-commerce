package com.example.order.order_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Order Service API", version = "1.0.0", description = "E-commerce Order Management Service", contact = @Contact(name = "Development Team", email = "dev@company.com"), license = @License(name = "MIT License", url = "https://opensource.org/licenses/MIT")), servers = {
        @Server(description = "Local Development Server", url = "http://localhost:8081"),
        @Server(description = "Docker Environment", url = "http://order-service-app:8081")
})
public class OpenApiConfig {
}