package com.example.UserMicroServiceProject.domain.model.user;

import com.example.UserMicroServiceProject.domain.model.valueobject.Address;
import com.example.UserMicroServiceProject.domain.model.valueobject.Email;
import com.example.UserMicroServiceProject.domain.model.valueobject.Password;
import com.example.UserMicroServiceProject.domain.model.valueobject.PersonName;
import com.example.UserMicroServiceProject.domain.model.valueobject.PhoneNumber;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserId;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA requirement
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // Builder pattern
public class User {
    @EmbeddedId
    private UserId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", unique = true, nullable = false))
    private Email email;

    @Embedded
    @AttributeOverride(name = "hashedValue", column = @Column(name = "password_hash", nullable = false))
    private Password password;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", nullable = false))
    })
    private PersonName name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "phone")),
            @AttributeOverride(name = "countryCode", column = @Column(name = "phone_country_code")),
            @AttributeOverride(name = "nationalNumber", column = @Column(name = "phone_national_number"))
    })
    private PhoneNumber phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.PENDING_VERIFICATION;

    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    @Column(name = "phone_verified", nullable = false)
    @Builder.Default
    private Boolean phoneVerified = false;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "locked_until")
    private Instant lockedUntil;

    // One-to-Many relationship with UserAddress
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<UserAddress> addresses = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // ================================
    // BUSINESS METHODS
    // ================================

    /**
     * Update user profile information
     * Business rule: Only active or pending verification users can update profile
     */
    public void updateProfile(PersonName newName, PhoneNumber newPhoneNumber) {
        if (!this.status.canBeUpdated()) {
            throw new IllegalStateException("Cannot update profile of user with status: " + this.status);
        }

        boolean phoneChanged = !this.phoneNumber.equals(newPhoneNumber);

        this.name = newName;
        this.phoneNumber = newPhoneNumber;

        // If phone changed, mark as unverified
        if (phoneChanged) {
            this.phoneVerified = false;
        }
    }

    /**
     * Change user password
     * Business rule: Must provide correct current password
     */
    public void changePassword(String currentRawPassword, Password newPassword) {
        if (!this.password.matches(currentRawPassword)) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (this.password.getHashedValue().equals(newPassword.getHashedValue())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }

        this.password = newPassword;
        this.failedLoginAttempts = 0; // Reset failed attempts on successful password change
    }

    /**
     * Verify user's email
     */
    public void verifyEmail() {
        this.emailVerified = true;

        // If both email and phone are verified, activate user
        if (this.phoneVerified && this.status == UserStatus.PENDING_VERIFICATION) {
            this.status = UserStatus.ACTIVE;
        }
    }

    /**
     * Verify user's phone number
     */
    public void verifyPhone() {
        this.phoneVerified = true;

        // If both email and phone are verified, activate user
        if (this.emailVerified && this.status == UserStatus.PENDING_VERIFICATION) {
            this.status = UserStatus.ACTIVE;
        }
    }

    /**
     * Activate user account
     * Business rule: Can only activate pending verification or inactive users
     */
    public void activate() {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("Cannot activate deleted user");
        }
        if (this.status == UserStatus.SUSPENDED) {
            throw new IllegalStateException("Cannot activate suspended user - requires admin intervention");
        }

        this.status = UserStatus.ACTIVE;
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
    }

    /**
     * Deactivate user account
     */
    public void deactivate() {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("User is already deleted");
        }

        this.status = UserStatus.INACTIVE;
    }

    /**
     * Suspend user account (admin action)
     */
    public void suspend() {
        if (this.status == UserStatus.DELETED) {
            throw new IllegalStateException("Cannot suspend deleted user");
        }

        this.status = UserStatus.SUSPENDED;
    }

    /**
     * Soft delete user account (GDPR compliance)
     */
    public void delete() {
        this.status = UserStatus.DELETED;
        // Note: In real implementation, you might want to anonymize data here
    }

    /**
     * Record successful login
     */
    public void recordSuccessfulLogin() {
        this.lastLoginAt = Instant.now();
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
    }

    /**
     * Record failed login attempt
     * Business rule: Lock account after 5 failed attempts for 30 minutes
     */
    public void recordFailedLoginAttempt() {
        final int MAX_FAILED_LOGIN_ATTEMP = 5;
        final int THIRTY_MINUTES_AS_SECONDS = 1800;
        this.failedLoginAttempts++;

        if (this.failedLoginAttempts >= MAX_FAILED_LOGIN_ATTEMP) {
            this.lockedUntil = Instant.now().plusSeconds(THIRTY_MINUTES_AS_SECONDS); // 30 minutes
        }
    }

    /**
     * Check if account is currently locked
     */
    public boolean isLocked() {
        if (this.lockedUntil == null) {
            return false;
        }

        if (Instant.now().isAfter(this.lockedUntil)) {
            // Lock expired, reset
            this.lockedUntil = null;
            this.failedLoginAttempts = 0;
            return false;
        }

        return true;
    }

    /**
     * Add new address to user
     * Business rule: Can have maximum 5 addresses
     */
    public void addAddress(Address address, boolean isDefault) {
        if (addresses.size() >= 5) {
            throw new IllegalStateException("User cannot have more than 5 addresses");
        }

        // If this is the first address or marked as default, set as default
        if (addresses.isEmpty() || isDefault) {
            setAllAddressesAsNonDefault();
        }

        UserAddress userAddress = UserAddress.builder()
                .id(UserAddressId.generate())
                .user(this)
                .address(address)
                .isDefault(addresses.isEmpty() || isDefault)
                .build();

        this.addresses.add(userAddress);
    }

    /**
     * Remove address from user
     * Business rule: Cannot remove if it's the only address and user has orders
     */
    public void removeAddress(UserAddressId addressId) {
        UserAddress addressToRemove = addresses.stream()
                .filter(addr -> addr.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        boolean wasDefault = addressToRemove.isDefault();
        this.addresses.remove(addressToRemove);

        // If removed address was default and there are other addresses, make first one default
        if (wasDefault && !addresses.isEmpty()) {
            addresses.get(0).setAsDefault();
        }
    }

    /**
     * Set specific address as default
     */
    public void setDefaultAddress(UserAddressId addressId) {
        UserAddress newDefaultAddress = addresses.stream()
                .filter(addr -> addr.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        setAllAddressesAsNonDefault();
        newDefaultAddress.setAsDefault();
    }

    private void setAllAddressesAsNonDefault() {
        addresses.forEach(addr -> addr.setAsNonDefault());
    }

    // ================================
    // QUERY METHODS
    // ================================

    public boolean isActive() {
        return this.status.isActive() && !isLocked();
    }

    public boolean canLogin() {
        return this.status.canLogin() && !isLocked();
    }

    public String getFullName() {
        return name.getFullName();
    }

    public String getDisplayName() {
        return name.getDisplayName();
    }

    public String getEmailValue() {
        return email.getValue();
    }

    public String getPhoneValue() {
        return phoneNumber != null ? phoneNumber.getValue() : null;
    }

    public Optional<UserAddress> getDefaultAddress() {
        return addresses.stream()
                .filter(UserAddress::isDefault)
                .findFirst();
    }

    public int getAddressCount() {
        return addresses.size();
    }

    public boolean hasVerifiedContact() {
        return emailVerified || phoneVerified;
    }

    public boolean isFullyVerified() {
        return emailVerified && phoneVerified;
    }

    // ================================
    // FACTORY METHODS
    // ================================

    /**
     * Create new user with email and password
     */
    public static User create(Email email, Password password, PersonName name) {
        return User.builder()
                .id(UserId.generate())
                .email(email)
                .password(password)
                .name(name)
                .status(UserStatus.PENDING_VERIFICATION)
                .emailVerified(false)
                .phoneVerified(false)
                .failedLoginAttempts(0)
                .build();
    }

    /**
     * Create new user with complete information
     */
    public static User create(Email email, Password password, PersonName name, PhoneNumber phoneNumber) {
        return User.builder()
                .id(UserId.generate())
                .email(email)
                .password(password)
                .name(name)
                .phoneNumber(phoneNumber)
                .status(UserStatus.PENDING_VERIFICATION)
                .emailVerified(false)
                .phoneVerified(false)
                .failedLoginAttempts(0)
                .build();
    }


    // ================================
    // EQUALITY AND HASH CODE
    // ================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email=" + email +
                ", name=" + name +
                ", status=" + status +
                '}';
    }
}