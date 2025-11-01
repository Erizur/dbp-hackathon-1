package com.example.oreo.sales.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.oreo.mail.EmailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReportListener {
    private final EmailService emailService;

    @EventListener
    @Async
    public void handleReportEvent(ReportEvent event) {
        // mandar correo when i can 
    }
}
