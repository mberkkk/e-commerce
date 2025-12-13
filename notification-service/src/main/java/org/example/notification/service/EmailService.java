package org.example.notification.service;

public interface EmailService {
    void sendWelcomeEmail(String to, String username);

    void sendOrderConfirmationEmail(String to, String orderId, String totalAmount);

    void sendCartAbandonedEmail(String to, String username);

    void sendPasswordChangedEmail(String to, String username);

    void sendStockUpdateEmail(String to, String productName);

    void sendCartReminderEmail(String userId, String productId);
}