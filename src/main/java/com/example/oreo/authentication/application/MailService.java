package com.example.oreo.authentication.application;

import lombok.RequiredArgsConstructor;

import java.util.Date;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    @Async
    public void resultMail(String to, String summaryMessage, Date dateFrom, Date dateTo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reporte Semanal Oreo - " + dateFrom.toString() + " a " + dateTo.toString());
        message.setText(summaryMessage);
        mailSender.send(message);
    }
}
