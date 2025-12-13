package com.example.UserMicroServiceProject.domain.model.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * User Trust Score Value Object
 * Represents user's trustworthiness score (0-100)
 */
@Getter
@EqualsAndHashCode
public class UserTrustScore {
    private final int value;

    public UserTrustScore(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Trust score must be between 0 and 100");
        }
        this.value = value;
    }

    public boolean isHighTrust() {
        return value >= 70;
    }

    public boolean isMediumTrust() {
        return value >= 40 && value < 70;
    }

    public boolean isLowTrust() {
        return value < 40;
    }

    public TrustLevel getTrustLevel() {
        if (isHighTrust()) return TrustLevel.HIGH;
        if (isMediumTrust()) return TrustLevel.MEDIUM;
        return TrustLevel.LOW;
    }

    public enum TrustLevel {
        LOW, MEDIUM, HIGH
    }

    @Override
    public String toString() {
        return "UserTrustScore{" + value + "/100, level=" + getTrustLevel() + "}";
    }
}
