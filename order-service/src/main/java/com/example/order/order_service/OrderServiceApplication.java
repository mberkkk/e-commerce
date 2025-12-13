package com.example.order.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = "com.example.order.order_service")
@EnableJpaRepositories(basePackages = "com.example.order.order_service.infrastructure.persistence.jpa.repository")
@EnableFeignClients(basePackages = "com.example.order.order_service.infrastructure.persistence.client.feign")
@EnableKafka // Kafka'yı aktif et
@EnableJpaAuditing
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}
}