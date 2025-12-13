package com.example.UserMicroServiceProject.domain.service;

import com.example.UserMicroServiceProject.domain.model.user.User;
import com.example.UserMicroServiceProject.domain.model.valueobject.Email;
import com.example.UserMicroServiceProject.domain.model.valueobject.Password;
import com.example.UserMicroServiceProject.domain.model.valueobject.PersonName;
import com.example.UserMicroServiceProject.domain.model.valueobject.PhoneNumber;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserStatus;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserTrustScore;
import org.springframework.stereotype.Service;

/**
 * UserDomainService
 *
 * Handles complex business logic that:
 * - Doesn't naturally fit in a single entity
 * - Involves multiple entities or value objects
 * - Contains domain rules that span across aggregates
 */
@Service
public class UserDomainService {

    /**
     * Create a new user with business validation
     *
     * Business Rules:
     * - Email must be unique (checked at application layer)
     * - Password must meet security requirements (validated in Password VO)
     * - Name must be properly formatted (validated in PersonName VO)
     * - User starts with PENDING_VERIFICATION status
     */
    public User createUser(Email email, Password password, PersonName name) {
        // Additional business logic for user creation
        validateUserCreationRules(email, name);

        return User.create(email, password, name);
    }

    /**
     * Create user with phone number
     */
    public User createUser(Email email, Password password, PersonName name, PhoneNumber phoneNumber) {
        validateUserCreationRules(email, name);
        validatePhoneNumberForRegistration(phoneNumber);

        return User.create(email, password, name, phoneNumber);
    }

    /**
     * Calculate user trust score based on various factors
     *
     * This is domain logic that involves multiple user properties
     * but doesn't belong to User entity as it's a calculation service
     */
    public UserTrustScore calculateTrustScore(User user) {
        int score = 0;

        // Email verification (+20 points)
        if (user.getEmailVerified()) {
            score += 20;
        }

        // Phone verification (+15 points)
        if (user.getPhoneVerified()) {
            score += 15;
        }

        // Account age (1 point per day, max 30)
        long daysSinceCreation = java.time.Duration.between(
                user.getCreatedAt(),
                java.time.Instant.now()
        ).toDays();
        score += Math.min((int) daysSinceCreation, 30);

        // Address count (5 points per address, max 25)
        score += Math.min(user.getAddressCount() * 5, 25);

        // Business email bonus (+10 points)
        if (user.getEmail().isBusinessEmail()) {
            score += 10;
        }

        // No failed login attempts bonus (+10 points)
        if (user.getFailedLoginAttempts() == 0) {
            score += 10;
        }

        // Account status penalty
        if (user.getStatus() == UserStatus.SUSPENDED) {
            score -= 50;
        }

        return new UserTrustScore(Math.max(0, Math.min(100, score)));
    }

    /**
     * Determine if user can perform sensitive operations
     *
     * Business Rule: High-trust users can perform sensitive operations
     */
    public boolean canPerformSensitiveOperation(User user) {
        UserTrustScore trustScore = calculateTrustScore(user);

        return user.isActive() &&
                user.isFullyVerified() &&
                trustScore.getValue() >= 70 &&
                user.getFailedLoginAttempts() == 0;
    }

    /**
     * Validate business rules for user creation
     */
    private void validateUserCreationRules(Email email, PersonName name) {
        // Business Rule: No disposable email domains
        if (isDisposableEmailDomain(email)) {
            throw new IllegalArgumentException("Disposable email domains are not allowed");
        }

        // Business Rule: Name cannot contain numbers or special characters
        if (containsInvalidCharacters(name)) {
            throw new IllegalArgumentException("Name cannot contain numbers or special characters");
        }
    }

    /**
     * Validate phone number for registration
     */
    private void validatePhoneNumberForRegistration(PhoneNumber phoneNumber) {
        // Business Rule: Only Turkish mobile numbers for registration
        if (!phoneNumber.isValidTurkishMobile()) {
            throw new IllegalArgumentException("Only Turkish mobile numbers are allowed for registration");
        }

        // Business Rule: Certain operators might be blocked (example)
        if ("BLOCKED_OPERATOR".equals(phoneNumber.getOperator())) {
            throw new IllegalArgumentException("This mobile operator is temporarily blocked");
        }
    }

    private boolean isDisposableEmailDomain(Email email) {
        String[] disposableDomains = {
                "tempmail.org", "10minutemail.com", "guerrillamail.com",
                "mailinator.com", "yopmail.com", "temp-mail.org"
        };

        String domain = email.getDomain().toLowerCase();
        for (String disposable : disposableDomains) {
            if (domain.equals(disposable)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsInvalidCharacters(PersonName name) {
        String fullName = name.getFullName();
        // Check for numbers or special characters (except Turkish characters)
        return fullName.matches(".*[0-9@#$%^&*()_+=\\[\\]{}|;:,.<>?].*");
    }
}
