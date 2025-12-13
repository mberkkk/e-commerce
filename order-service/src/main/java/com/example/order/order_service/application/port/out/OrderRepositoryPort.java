 package com.example.order.order_service.application.port.out;

  //OrderRepositoryPort.java

 import com.example.order.order_service.domain.model.Order;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;

 import java.util.Optional;

 public interface OrderRepositoryPort {
     Order save(Order order);
     Optional<Order> findById(String orderId);
     Page<Order> findByCustomerId(String customerId, Pageable pageable);
     void delete(Order order);  //Eğer gerekirse
 }


