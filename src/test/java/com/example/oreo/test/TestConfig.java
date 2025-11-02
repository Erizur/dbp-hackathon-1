package com.example.oreo.test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import jakarta.mail.internet.MimeMessage;

@TestConfiguration
public class TestConfig {

    @Bean
    public JavaMailSender noopMailSender() {
        return new JavaMailSender() {
            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException { /* no-op */ }

            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException { /* no-op */ }

            @Override
            public MimeMessage createMimeMessage() { return null; }

            @Override
            public MimeMessage createMimeMessage(java.io.InputStream contentStream) throws MailException { return null; }

            @Override
            public void send(MimeMessage mimeMessage) throws MailException { /* no-op */ }

            @Override
            public void send(MimeMessage... mimeMessages) throws MailException { /* no-op */ }
        };
    }
}
