package com.example.UserMicroServiceProject.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * PhoneNumber Value Object
 *
 * Encapsulates:
 * - Turkish phone number validation
 * - Phone number normalization (+90 format)
 * - Country code extraction
 * - Formatted display
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneNumber {

    // Turkish mobile phone pattern: +90 5XX XXX XX XX
    private static final Pattern TURKEY_MOBILE_PATTERN = Pattern.compile(
            "^(\\+90|0)?[5][0-9]{9}$"
    );

    private String value;
    private String countryCode;
    private String nationalNumber;

    public PhoneNumber(String value) {
        this.value = validateAndNormalize(value);
        parsePhoneNumber(this.value);
    }

    private String validateAndNormalize(String phoneValue) {
        if (phoneValue == null || phoneValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }

        // Remove all non-digit characters except +
        String normalized = phoneValue.replaceAll("[^+\\d]", "");

        // Validate Turkish mobile number format
        if (!TURKEY_MOBILE_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    "Invalid Turkish mobile phone number format. Expected format: +90 5XX XXX XX XX"
            );
        }

        // Normalize to +90 format
        if (normalized.startsWith("0")) {
            normalized = "+90" + normalized.substring(1);
        } else if (normalized.startsWith("5")) {
            normalized = "+90" + normalized;
        } else if (!normalized.startsWith("+90")) {
            throw new IllegalArgumentException("Only Turkish phone numbers are supported");
        }

        return normalized;
    }

    private void parsePhoneNumber(String normalizedValue) {
        this.countryCode = "+90";
        this.nationalNumber = normalizedValue.substring(3); // Remove +90
    }

    /**
     * Get formatted phone number: +90 555 123 45 67
     */
    public String getFormattedNumber() {
        if (nationalNumber.length() == 10) {
            return String.format("%s %s %s %s %s",
                    countryCode,
                    nationalNumber.substring(0, 3),  // 555
                    nationalNumber.substring(3, 6),  // 123
                    nationalNumber.substring(6, 8),  // 45
                    nationalNumber.substring(8, 10)  // 67
            );
        }
        return value;
    }

    /**
     * Check if it's a valid Turkish mobile number
     */
    public boolean isValidTurkishMobile() {
        return nationalNumber.startsWith("5") && nationalNumber.length() == 10;
    }

    /**
     * Get mobile operator (basic detection)
     */
    public String getOperator() {
        if (!isValidTurkishMobile()) {
            return "UNKNOWN";
        }

        String prefix = nationalNumber.substring(1, 3); // Get 2nd and 3rd digits

        // Turkish mobile operators (simplified)
        if (prefix.matches("(53|54|55)")) return "TURKCELL";
        if (prefix.matches("(50|51|52|56|57)")) return "VODAFONE";
        if (prefix.matches("(58|59)")) return "TURK_TELEKOM";

        return "OTHER";
    }

    @Override
    public String toString() {
        return value;
    }

    public static PhoneNumber of(String value) {
        return new PhoneNumber(value);
    }
}
