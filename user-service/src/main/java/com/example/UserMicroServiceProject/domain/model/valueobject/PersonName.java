package com.example.UserMicroServiceProject.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

/**
 * PersonName Value Object
 *
 * Encapsulates:
 * - Name validation (Turkish characters support)
 * - Name normalization (proper case)
 * - Full name composition
 * - Initials generation
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonName {

    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-ZçğıöşüÇĞIİÖŞÜ\\s]{2,50}$"
    );

    private String firstName;
    private String lastName;

    public PersonName(String firstName, String lastName) {
        this.firstName = validateAndNormalize(firstName, "First name");
        this.lastName = validateAndNormalize(lastName, "Last name");
    }

    private String validateAndNormalize(String name, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }

        String normalized = name.trim();

        if (!NAME_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException(
                    fieldName + " must contain only letters and be between 2-50 characters"
            );
        }

        return capitalizeFirstLetter(normalized);
    }

    private String capitalizeFirstLetter(String text) {
        if (text.length() == 0) return text;

        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getInitials() {
        return firstName.charAt(0) + "." + lastName.charAt(0) + ".";
    }

    public String getDisplayName() {
        return firstName; // Only first name for casual display
    }

    @Override
    public String toString() {
        return getFullName();
    }

    public static PersonName of(String firstName, String lastName) {
        return new PersonName(firstName, lastName);
    }
}