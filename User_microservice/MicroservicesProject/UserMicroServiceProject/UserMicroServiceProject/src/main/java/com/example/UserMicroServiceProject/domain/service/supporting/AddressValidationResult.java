package com.example.UserMicroServiceProject.domain.service.supporting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Address Validation Result Value Object
 *
 * Encapsulates the result of address validation
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressValidationResult {

    private final boolean valid;
    private final String errorMessage;
    private final List<String> warnings;

    public static AddressValidationResult success() {
        return new AddressValidationResult(true, null, new ArrayList<>());
    }

    public static AddressValidationResult successWithWarnings(List<String> warnings) {
        return new AddressValidationResult(true, null, new ArrayList<>(warnings));
    }

    public static AddressValidationResult failure(String errorMessage) {
        return new AddressValidationResult(false, errorMessage, new ArrayList<>());
    }

    public boolean isValid() {
        return valid;
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public int getWarningCount() {
        return warnings.size();
    }

    @Override
    public String toString() {
        if (valid) {
            return "AddressValidationResult{valid=true, warnings=" + warnings.size() + "}";
        } else {
            return "AddressValidationResult{valid=false, error='" + errorMessage + "'}";
        }
    }
}
