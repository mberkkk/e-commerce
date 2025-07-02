package com.microservices.cart_service.Repository;

import com.microservices.cart_service.Entity.Cart;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserId(String userId);

    @Query("SELECT c FROM Cart c WHERE c.updatedAt <= :threshold")
    List<Cart> findInactiveCarts(@Param("threshold") LocalDateTime threshold);
}
