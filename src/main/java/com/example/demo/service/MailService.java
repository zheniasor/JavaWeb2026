package com.example.demo.service;

import com.example.demo.entity.User;

public interface MailService {
    void sendConfirmationEmail(User user);

    void sendPasswordResetEmail(String email, String resetToken);

   void sendNotification(String to, String subject, String content);
}