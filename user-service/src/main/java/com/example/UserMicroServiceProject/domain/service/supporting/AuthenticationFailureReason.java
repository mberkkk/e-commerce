package com.example.UserMicroServiceProject.domain.service.supporting;

public enum AuthenticationFailureReason {
    INVALID_CREDENTIALS("Invalid email or password"),
    ACCOUNT_LOCKED("Account is temporarily locked"),
    ACCOUNT_SUSPENDED("Account has been suspended"),
    ACCOUNT_INACTIVE("Account is inactive"),
    EMAIL_NOT_VERIFIED("Email address is not verified"),
    ACCOUNT_DELETED("Account has been deleted"),
    TOO_MANY_ATTEMPTS("Too many failed login attempts"),
    INVALID_USER("User does not exist");

    private final String description;

    AuthenticationFailureReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRecoverable() {
        return this == ACCOUNT_LOCKED ||
                this == EMAIL_NOT_VERIFIED ||
                this == INVALID_CREDENTIALS;
    }

    public boolean requiresAdminIntervention() {
        return this == ACCOUNT_SUSPENDED ||
                this == ACCOUNT_DELETED;
    }
}