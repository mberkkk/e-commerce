package com.microservices.product_service.product.entity;

import com.microservices.product_service.category.entity.Category;
import com.microservices.product_service.common.entity.BaseEntity;
import com.microservices.product_service.common.exception.DomainException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Builder.Default
    private String currency = "TL";

    @Column(nullable = false)
    private Integer stockQuantity;

    private String imageUrl;

    @Builder.Default
    private Integer popularityScore = 0;

    @ManyToOne(fetch = FetchType.LAZY) // Performance: Lazy load categories
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    private Boolean isActive = true;

    // ========================================================================
    // RICH DOMAIN METHODS (The "Brain" of the class)
    // ========================================================================

    /**
     * Reserves stock for an order.
     * Enforces the rule: You cannot sell what you don't have.
     */
    public void reserveStock(int quantity) {
        if (quantity <= 0) {
            throw new DomainException("Reservation quantity must be positive.");
        }
        if (this.stockQuantity < quantity) {
            throw new DomainException("Insufficient stock for product: " + this.name);
        }
        this.stockQuantity -= quantity;
        this.popularityScore += 1; // Business Rule: Buying increases popularity
    }

    /**
     * Restores stock (e.g., if an order is cancelled).
     */
    public void releaseStock(int quantity) {
        if (quantity <= 0) {
            throw new DomainException("Release quantity must be positive.");
        }
        this.stockQuantity += quantity;
    }

    /**
     * Updates price with validation.
     * Enforces the rule: Price cannot be negative.
     */
    public void updatePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("Price cannot be negative.");
        }
        this.price = newPrice;
    }

    public void updateInventory(Integer newQuantity) {
        if (newQuantity == null || newQuantity < 0) {
            throw new DomainException("Inventory quantity cannot be negative.");
        }
        this.stockQuantity = newQuantity;
    }

    /**
     * Increases product popularity.
     * Handles null safety internally so the Service doesn't have to.
     */
    public void increasePopularity(Integer scoreDelta) {
        if (scoreDelta == null) return;

        // Handle potential null in legacy data safely
        int current = (this.popularityScore == null) ? 0 : this.popularityScore;
        this.popularityScore = current + scoreDelta;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    // Helper to check availability without throwing exception
    public boolean isStockAvailable(int quantity) {
        return this.isActive && this.stockQuantity >= quantity;
    }
}