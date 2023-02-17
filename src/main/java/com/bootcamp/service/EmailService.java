package com.bootcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("akashsudan555@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
        System.out.println("Mail Sent");
    }
}
