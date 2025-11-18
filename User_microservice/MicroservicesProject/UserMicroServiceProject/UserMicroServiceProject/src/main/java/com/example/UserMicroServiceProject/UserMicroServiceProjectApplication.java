package com.example.UserMicroServiceProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EntityScan(basePackages = "com.example.UserMicroServiceProject.domain.model")
@EnableKafka
@EnableFeignClients(basePackages = "com.example.UserMicroServiceProject.infrastructure.external")
public class UserMicroServiceProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMicroServiceProjectApplication.class, args);
	}

}
