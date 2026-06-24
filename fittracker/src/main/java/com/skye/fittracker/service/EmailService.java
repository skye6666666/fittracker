package com.skye.fittracker.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(
            String to,
            String token
    ) {

        String verifyUrl =
                "http://localhost:8080/api/users/verify?token="
                        + token;

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);

        message.setSubject(
                "Verify your FitTracker account"
        );

        message.setText(
                "Click the link below:\n\n"
                        + verifyUrl
        );

        mailSender.send(message);
    }
}
