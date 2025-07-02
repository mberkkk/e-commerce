package org.example.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.notification.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendWelcomeEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to Our E-Commerce Platform!");
        message.setText("Dear " + username + ",\n\nWelcome to our platform! We're excited to have you on board.");
        mailSender.send(message);
    }

    @Override
    public void sendOrderConfirmationEmail(String to, String orderId, String totalAmount) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Order Confirmation - Order #" + orderId);
        message.setText("Thank you for your order!\n\nOrder ID: " + orderId + "\nTotal Amount: " + totalAmount);
        mailSender.send(message);
    }

    @Override
    public void sendCartAbandonedEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Don't Forget Your Cart!");
        message.setText("Dear " + username
                + ",\n\nYou have items in your cart waiting for you. Don't miss out on these great products!");
        mailSender.send(message);
    }

    @Override
    public void sendPasswordChangedEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Changed Successfully");
        message.setText("Dear " + username + ",\n\nYour password has been successfully changed.");
        mailSender.send(message);
    }

    @Override
    public void sendStockUpdateEmail(String to, String productName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Product Stock Update");
        message.setText("The product '" + productName + "' is now back in stock!");
        mailSender.send(message);
    }

    @Override
    public void sendCartReminderEmail(String userId, String productId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("user-" + userId + "@example.com"); // Mock email address
        message.setSubject("Item Added to Cart - Complete Your Purchase");
        message.setText(
                "Dear User,\n\nYou've added an item to your cart. Don't forget to complete your purchase to secure your items!");
        mailSender.send(message);
    }
}