package com.example.UserMicroServiceProject.domain.service.supporting;

public enum AddressSuggestionType {
    POSTAL_CODE("Postal Code", "Consider adding or correcting postal code"),
    DISTRICT("District", "District information can be improved"),
    TITLE("Title", "Address title can be more descriptive"),
    FORMATTING("Formatting", "Address formatting can be improved"),
    COMPLETENESS("Completeness", "Address information can be more complete"),
    ACCURACY("Accuracy", "Address accuracy can be verified");

    private final String displayName;
    private final String description;

    AddressSuggestionType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCritical() {
        return this == POSTAL_CODE || this == ACCURACY;
    }

    public boolean isOptional() {
        return this == TITLE || this == FORMATTING;
    }
}