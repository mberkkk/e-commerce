package com.example.order.order_service.infrastructure.persistence.jpa.repository;



import com.example.order.order_service.infrastructure.persistence.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {
    Page<OrderEntity> findByCustomerId(String customerId, Pageable pageable); }
