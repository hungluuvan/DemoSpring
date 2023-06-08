package com.mor.backend.services;

public interface EmailService {
    void sendEmail(String to,  String subject, String body);
}
