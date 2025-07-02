package com.microservices.product_service.Repository;

import com.microservices.product_service.Entity.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockReservationRepository extends JpaRepository<StockReservation,Long> {
    Optional<StockReservation> findByOrderReference(String orderReference);

    Optional<StockReservation> findById(Long id);
}
