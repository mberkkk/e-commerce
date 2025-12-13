package com.example.UserMicroServiceProject.domain.model.valueobject;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

/**
 * Password Value Object
 *
 * Encapsulates:
 * - Password strength validation
 * - BCrypt hashing
 * - Common password detection
 * - Password matching
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])[A-Za-z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{8,}$"
    );

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private static final String[] COMMON_PASSWORDS = {
            "password", "123456", "123456789", "qwerty", "abc123",
            "password123", "admin", "letmein", "welcome", "monkey",
            "1234567890", "dragon", "superman", "batman", "master"
    };

    private String hashedValue;

    private Password(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    /**
     * Create password from raw string - validates and hashes
     */
    public static Password fromRawPassword(String rawPassword) {
        validatePasswordStrength(rawPassword);
        String hashed = encoder.encode(rawPassword);
        return new Password(hashed);
    }

    /**
     * Create password from already hashed value - for database loading
     */
    public static Password fromHashedPassword(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        return new Password(hashedPassword);
    }

    private static void validatePasswordStrength(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (rawPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if (rawPassword.length() > 128) {
            throw new IllegalArgumentException("Password is too long (max 128 characters)");
        }

        if (!PASSWORD_PATTERN.matcher(rawPassword).matches()) {
            throw new IllegalArgumentException(
                    "Password must contain at least one lowercase letter, " +
                            "one uppercase letter, one digit, and one special character"
            );
        }

        if (isCommonPassword(rawPassword)) {
            throw new IllegalArgumentException("Password is too common, please choose a stronger one");
        }
    }

    private static boolean isCommonPassword(String password) {
        String lowercasePassword = password.toLowerCase();

        for (String common : COMMON_PASSWORDS) {
            if (lowercasePassword.contains(common)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if raw password matches this hashed password
     */
    public boolean matches(String rawPassword) {
        return encoder.matches(rawPassword, hashedValue);
    }

    /**
     * Check if password hash needs upgrade (for security improvements)
     */
    public boolean needsRehashing() {
        return !encoder.upgradeEncoding(hashedValue);
    }

    // Security: Don't expose hash in toString
    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}
