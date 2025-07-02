package com.example.UserMicroServiceProject.domain.model.user;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * UserAddressId Value Object
 *
 * Unique identifier for user addresses
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAddressId implements Serializable {

    private static final long serialVersionUID = -933657376487588292L;
    private String value;

    private UserAddressId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("UserAddressId cannot be null or empty");
        }

        // Validate UUID format
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UserAddressId must be a valid UUID format", e);
        }

        this.value = value;
    }

    public static UserAddressId generate() {
        return new UserAddressId(UUID.randomUUID().toString());
    }

    public static UserAddressId of(String value) {
        return new UserAddressId(value);
    }

    public UUID toUUID() {
        return UUID.fromString(value);
    }

    @Override
    public String toString() {
        return value;
    }
}