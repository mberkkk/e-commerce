package com.example.UserMicroServiceProject.domain.service.supporting;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Password Strength Score Value Object
 *
 * Represents password strength score (0-100)
 */
@Getter
@EqualsAndHashCode
public class PasswordStrengthScore {

    private final int value;

    public PasswordStrengthScore(int value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Password strength score must be between 0 and 100");
        }
        this.value = value;
    }

    public PasswordStrengthLevel getStrengthLevel() {
        if (value >= 80) return PasswordStrengthLevel.VERY_STRONG;
        if (value >= 60) return PasswordStrengthLevel.STRONG;
        if (value >= 40) return PasswordStrengthLevel.MEDIUM;
        if (value >= 20) return PasswordStrengthLevel.WEAK;
        return PasswordStrengthLevel.VERY_WEAK;
    }

    public boolean isAcceptable() {
        return value >= 40; // Medium or above
    }

    public boolean isStrong() {
        return value >= 60;
    }

    public boolean isVeryStrong() {
        return value >= 80;
    }

    public String getRecommendation() {
        switch (getStrengthLevel()) {
            case VERY_WEAK:
                return "Very weak password. Please use at least 8 characters with uppercase, lowercase, numbers and special characters.";
            case WEAK:
                return "Weak password. Consider adding more character variety and length.";
            case MEDIUM:
                return "Medium strength password. Consider making it longer and avoiding common patterns.";
            case STRONG:
                return "Strong password. Good security level.";
            case VERY_STRONG:
                return "Very strong password. Excellent security.";
            default:
                return "Unknown strength level.";
        }
    }

    public enum PasswordStrengthLevel {
        VERY_WEAK("Very Weak", "#ff4444"),
        WEAK("Weak", "#ff8800"),
        MEDIUM("Medium", "#ffdd00"),
        STRONG("Strong", "#88cc00"),
        VERY_STRONG("Very Strong", "#00cc44");

        private final String displayName;
        private final String colorCode;

        PasswordStrengthLevel(String displayName, String colorCode) {
            this.displayName = displayName;
            this.colorCode = colorCode;
        }

        public String getDisplayName() { return displayName; }
        public String getColorCode() { return colorCode; }
    }

    @Override
    public String toString() {
        return "PasswordStrengthScore{" + value + "/100, level=" + getStrengthLevel() + "}";
    }
}
