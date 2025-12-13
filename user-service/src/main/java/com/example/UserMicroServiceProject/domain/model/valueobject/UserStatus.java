package com.example.UserMicroServiceProject.domain.model.valueobject;

public enum UserStatus {
    ACTIVE("Active user account"),
    INACTIVE("Temporarily inactive account"),
    SUSPENDED("Suspended due to policy violation"),
    PENDING_VERIFICATION("Pending email/phone verification"),
    DELETED("Account marked for deletion");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canLogin() {
        return this == ACTIVE || this == PENDING_VERIFICATION;
    }

    public boolean canBeUpdated() {
        return this == ACTIVE || this == PENDING_VERIFICATION;
    }
}
