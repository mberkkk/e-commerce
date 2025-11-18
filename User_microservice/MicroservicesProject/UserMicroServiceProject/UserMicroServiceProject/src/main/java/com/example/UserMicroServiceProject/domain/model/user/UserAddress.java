package com.example.UserMicroServiceProject.domain.model.user;

import com.example.UserMicroServiceProject.domain.model.valueobject.Address;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

/**
 * UserAddress Entity
 *
 * Represents a user's address with additional metadata:
 * - Default address flag
 * - Creation timestamp
 * - Relationship to User
 */
@Entity
@Table(name = "user_addresses")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAddress {

    @EmbeddedId
    private UserAddressId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "title", column = @Column(name = "title", nullable = false)),
            @AttributeOverride(name = "fullAddress", column = @Column(name = "full_address", nullable = false, columnDefinition = "TEXT")),
            @AttributeOverride(name = "city", column = @Column(name = "city", nullable = false)),
            @AttributeOverride(name = "district", column = @Column(name = "district")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "postal_code"))
    })
    private Address address;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // ================================
    // BUSINESS METHODS
    // ================================

    /**
     * Set this address as default
     * Note: Caller should ensure other addresses are set to non-default
     */
    public void setAsDefault() {
        this.isDefault = true;
    }

    /**
     * Set this address as non-default
     */
    public void setAsNonDefault() {
        this.isDefault = false;
    }

    /**
     * Update address information
     */
    public void updateAddress(Address newAddress) {
        this.address = newAddress;
    }

    // ================================
    // QUERY METHODS
    // ================================

    public boolean isDefault() {
        return this.isDefault;
    }

    public String getTitle() {
        return address.getTitle();
    }

    public String getFullAddress() {
        return address.getFullAddress();
    }

    public String getCity() {
        return address.getCity();
    }

    public String getShortAddress() {
        return address.getShortAddress();
    }

    public String getCompleteAddress() {
        return address.getCompleteAddress();
    }

    public boolean isBusinessAddress() {
        return address.isBusinessAddress();
    }

    // ================================
    // EQUALITY AND HASH CODE
    // ================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAddress)) return false;
        UserAddress that = (UserAddress) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserAddress{" +
                "id=" + id +
                ", address=" + address +
                ", isDefault=" + isDefault +
                '}';
    }
}
