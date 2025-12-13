package com.example.UserMicroServiceProject.infrastructure.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.example.UserMicroServiceProject.domain.model")
@EnableJpaRepositories(basePackages = "com.example.UserMicroServiceProject.infrastructure.persistence")
public class JpaConfig {
}