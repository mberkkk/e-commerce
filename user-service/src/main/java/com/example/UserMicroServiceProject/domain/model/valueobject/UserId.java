package com.example.UserMicroServiceProject.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * UserId Value Object
 *
 * Encapsulates:
 * - Unique user identification
 * - UUID generation and validation
 * - Type safety (prevents mixing with other IDs)
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId implements Serializable {

    private static final long serialVersionUID = -5737275817681403912L;
    private String id;

    private UserId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("UserId cannot be null or empty");
        }

        // Validate UUID format
        try {
            UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UserId must be a valid UUID format", e);
        }

        this.id = id;
    }

    /**
     * Generate new unique UserId
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }

    /**
     * Create UserId from existing string value
     */
    public static UserId of(String value) {
        return new UserId(value);
    }

    /**
     * Get the UUID object representation
     */
    public UUID toUUID() {
        return UUID.fromString(id);
    }

    @Override
    public String toString() {
        return id;
    }
}
