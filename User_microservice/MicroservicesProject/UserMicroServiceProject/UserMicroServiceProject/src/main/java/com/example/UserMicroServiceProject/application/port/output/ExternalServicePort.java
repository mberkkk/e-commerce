package com.example.UserMicroServiceProject.application.port.output;

import com.example.UserMicroServiceProject.domain.model.valueobject.Email;
import com.example.UserMicroServiceProject.domain.model.valueobject.PhoneNumber;
import com.example.UserMicroServiceProject.domain.model.valueobject.UserId;

public interface ExternalServicePort {

    // Email Service Operations
    EmailResult sendWelcomeEmail(Email email, String userName);
    EmailResult sendPasswordResetEmail(Email email, String resetToken);
    EmailResult sendVerificationEmail(Email email, String verificationCode);

    // SMS Service Operations
    SmsResult sendPhoneVerification(PhoneNumber phoneNumber, String verificationCode);
    SmsResult sendSecurityAlert(PhoneNumber phoneNumber, String alertMessage);

    // Other Microservice Integration
    CartServiceResult clearUserCart(UserId userId);
    OrderServiceResult getUserOrderCount(UserId userId);
    NotificationServiceResult getNotificationPreferences(UserId userId);
}