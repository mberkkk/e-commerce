package com.example.UserMicroServiceProject.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * Email Value Object
 *
 * Encapsulates:
 * - Email format validation
 * - Temporary email domain blocking
 * - Email normalization (lowercase, trimming)
 * - Domain extraction utilities
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA requirement
public class Email {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );

    private static final String[] BLOCKED_DOMAINS = {
            "10minutemail.com", "guerrillamail.com", "tempmail.org", "mailinator.com"
    };

    private String value;

    public Email(String value) {
        this.value = validateAndNormalize(value);
    }

    private String validateAndNormalize(String emailValue) {
        if (emailValue == null || emailValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        String normalizedEmail = emailValue.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + emailValue);
        }

        if (normalizedEmail.length() > 255) {
            throw new IllegalArgumentException("Email is too long (max 255 characters)");
        }

        if (isBlockedDomain(normalizedEmail)) {
            throw new IllegalArgumentException("Temporary email domains are not allowed");
        }

        return normalizedEmail;
    }

    private boolean isBlockedDomain(String email) {
        String domain = email.substring(email.indexOf("@") + 1);

        for (String blockedDomain : BLOCKED_DOMAINS) {
            if (blockedDomain.equals(domain)) {
                return true;
            }
        }
        return false;
    }

    public String getDomain() {
        return value.substring(value.indexOf("@") + 1);
    }

    public String getLocalPart() {
        return value.substring(0, value.indexOf("@"));
    }

    public boolean isBusinessEmail() {
        String[] businessDomains = {"gmail.com", "yahoo.com", "outlook.com", "hotmail.com"};
        String domain = getDomain();

        for (String businessDomain : businessDomains) {
            if (businessDomain.equals(domain)) {
                return false; // Personal email
            }
        }
        return true; // Likely business email
    }

    @Override
    public String toString() {
        return value;
    }

    public static Email of(String value) {
        return new Email(value);
    }
}