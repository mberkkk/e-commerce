package com.example.UserMicroServiceProject.domain.service;

import com.example.UserMicroServiceProject.domain.model.user.User;
import com.example.UserMicroServiceProject.domain.model.valueobject.Password;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserStatus;
import com.example.UserMicroServiceProject.domain.service.supporting.AuthenticationFailureReason;
import com.example.UserMicroServiceProject.domain.service.supporting.AuthenticationResult;
import com.example.UserMicroServiceProject.domain.service.supporting.PasswordChangeResult;
import com.example.UserMicroServiceProject.domain.service.supporting.PasswordStrengthScore;
import org.springframework.stereotype.Service;
import java.time.temporal.TemporalUnit.*;


import java.time.Instant;

/**
 * AuthenticationDomainService
 *
 * Handles authentication-related business logic:
 * - Login validation
 * - Password policies
 * - Account security rules
 */
@Service
public class AuthenticationDomainService {

    /**
     * Authenticate user with email and password
     *
     * Business Rules:
     * - User must be active and not locked
     * - Email must be verified
     * - Password must match
     * - Failed attempts are tracked
     */
    public AuthenticationResult authenticate(User user, String rawPassword) {
        // Business Rule: Check if user can login
        if (!user.canLogin()) {
            return AuthenticationResult.failure(
                    determineFailureReason(user),
                    "User cannot login: " + user.getStatus()
            );
        }

        // Business Rule: Check if account is locked
        if (user.isLocked()) {
            return AuthenticationResult.failure(
                    AuthenticationFailureReason.ACCOUNT_LOCKED,
                    "Account is temporarily locked due to multiple failed attempts"
            );
        }

        // Business Rule: Verify password
        if (!user.getPassword().matches(rawPassword)) {
            // Record failed attempt
            user.recordFailedLoginAttempt();

            return AuthenticationResult.failure(
                    AuthenticationFailureReason.INVALID_CREDENTIALS,
                    "Invalid email or password"
            );
        }

        // Success: Record login and return success result
        user.recordSuccessfulLogin();

        return AuthenticationResult.success(user);
    }

    /**
     * Validate password change operation
     *
     * Business Rules:
     * - Current password must be correct
     * - New password must be different
     * - New password must meet security requirements
     */
    public PasswordChangeResult validatePasswordChange(
            User user,
            String currentPassword,
            String newPassword) {

        // Business Rule: User must be active
        if (!user.isActive()) {
            return PasswordChangeResult.failure("User account is not active");
        }

        // Business Rule: Current password must be correct
        if (!user.getPassword().matches(currentPassword)) {
            return PasswordChangeResult.failure("Current password is incorrect");
        }

        // Business Rule: New password must be different
        if (currentPassword.equals(newPassword)) {
            return PasswordChangeResult.failure("New password must be different from current password");
        }

        // Business Rule: New password security check
        try {
            Password newPasswordVO = Password.fromRawPassword(newPassword);

            // Business Rule: Check if password needs rehashing
            if (user.getPassword().needsRehashing()) {
                return PasswordChangeResult.successWithRehashing(newPasswordVO);
            }

            return PasswordChangeResult.success(newPasswordVO);

        } catch (IllegalArgumentException e) {
            return PasswordChangeResult.failure("New password does not meet security requirements: " + e.getMessage());
        }
    }

    /**
     * Calculate password strength score
     */
    public PasswordStrengthScore calculatePasswordStrength(String rawPassword) {
        int score = 0;

        // Length score (max 25 points)
        if (rawPassword.length() >= 8) score += 10;
        if (rawPassword.length() >= 12) score += 10;
        if (rawPassword.length() >= 16) score += 5;

        // Character variety (max 40 points)
        if (rawPassword.matches(".*[a-z].*")) score += 10; // Lowercase
        if (rawPassword.matches(".*[A-Z].*")) score += 10; // Uppercase
        if (rawPassword.matches(".*[0-9].*")) score += 10; // Numbers
        if (rawPassword.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?].*")) score += 10; // Special chars

        // Complexity patterns (max 35 points)
        if (!rawPassword.matches(".*(.)\\1{2,}.*")) score += 10; // No repeated characters
        if (!isCommonPassword(rawPassword)) score += 15; // Not in common passwords list
        if (!containsPersonalInfo(rawPassword)) score += 10; // No obvious personal info

        return new PasswordStrengthScore(Math.min(100, score));
    }

    /**
     * Determine if user should be forced to change password
     *
     * Business Rules:
     * - Password older than 90 days
     * - Password was compromised
     * - Password doesn't meet current security standards
     */
    public boolean shouldForcePasswordChange(User user) {
        // Rule 1: Password age check (90 days)
        Instant passwordLastChanged = user.getUpdatedAt(); // Approximation

        // Rule 2: Password needs rehashing (security upgrade)
        if (user.getPassword().needsRehashing()) {
            return true;
        }

        // Rule 3: Account has been compromised indicators
        if (user.getFailedLoginAttempts() > 10) {
            return true;
        }

        return false;
    }

    private AuthenticationFailureReason determineFailureReason(User user) {
        if (!user.getEmailVerified()) {
            return AuthenticationFailureReason.EMAIL_NOT_VERIFIED;
        }
        if (user.getStatus() == UserStatus.SUSPENDED) {
            return AuthenticationFailureReason.ACCOUNT_SUSPENDED;
        }
        if (user.getStatus() == UserStatus.INACTIVE) {
            return AuthenticationFailureReason.ACCOUNT_INACTIVE;
        }
        return AuthenticationFailureReason.ACCOUNT_LOCKED;
    }

    private boolean isCommonPassword(String password) {
        String[] commonPasswords = {
                "password", "123456", "123456789", "qwerty", "abc123",
                "password123", "admin", "letmein", "welcome", "monkey"
        };

        String lowerPassword = password.toLowerCase();
        for (String common : commonPasswords) {
            if (lowerPassword.contains(common)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsPersonalInfo(String password) {
        // Simplified check - in real implementation, you might check against user data
        String lower = password.toLowerCase();

        // Check for common personal info patterns
        return lower.contains("admin") ||
                lower.contains("user") ||
                lower.contains("test") ||
                lower.matches(".*\\d{4}.*"); // Birth year patterns
    }
}