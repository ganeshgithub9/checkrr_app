package com.example.checkrr.service;

import jakarta.mail.MessagingException;

public interface MailService {
    void sendTestEmail(String to) throws MessagingException;
}
