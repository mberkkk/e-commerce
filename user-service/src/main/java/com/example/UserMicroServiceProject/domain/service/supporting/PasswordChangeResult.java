package com.example.UserMicroServiceProject.domain.service.supporting;

import com.example.UserMicroServiceProject.domain.model.valueobject.Password;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Password Change Result Value Object
 *
 * Encapsulates the result of password change operation
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordChangeResult {

    private final boolean successful;
    private final Password newPassword;
    private final String errorMessage;
    private final boolean requiresRehashing;

    public static PasswordChangeResult success(Password newPassword) {
        return new PasswordChangeResult(true, newPassword, null, false);
    }

    public static PasswordChangeResult successWithRehashing(Password newPassword) {
        return new PasswordChangeResult(true, newPassword, null, true);
    }

    public static PasswordChangeResult failure(String errorMessage) {
        return new PasswordChangeResult(false, null, errorMessage, false);
    }

    public boolean isSuccessful() {
        return successful;
    }

    public boolean hasNewPassword() {
        return newPassword != null;
    }

    @Override
    public String toString() {
        if (successful) {
            return "PasswordChangeResult{successful=true, requiresRehashing=" + requiresRehashing + "}";
        } else {
            return "PasswordChangeResult{successful=false, error='" + errorMessage + "'}";
        }
    }
}
