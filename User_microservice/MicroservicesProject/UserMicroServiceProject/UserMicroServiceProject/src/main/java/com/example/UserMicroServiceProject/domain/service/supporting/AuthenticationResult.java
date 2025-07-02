package com.example.UserMicroServiceProject.domain.service.supporting;

import com.example.UserMicroServiceProject.domain.model.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Authentication Result Value Object
 *
 * Encapsulates the result of authentication operation
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationResult {

    private final boolean successful;
    private final User user;
    private final AuthenticationFailureReason failureReason;
    private final String message;

    public static AuthenticationResult success(User user) {
        return new AuthenticationResult(true, user, null, "Authentication successful");
    }

    public static AuthenticationResult failure(AuthenticationFailureReason reason, String message) {
        return new AuthenticationResult(false, null, reason, message);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public boolean hasUser() {
        return user != null;
    }

    @Override
    public String toString() {
        if (successful) {
            return "AuthenticationResult{successful=true, user=" + user.getId() + "}";
        } else {
            return "AuthenticationResult{successful=false, reason=" + failureReason + ", message='" + message + "'}";
        }
    }
}
