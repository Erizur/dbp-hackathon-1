package com.example.oreo.authentication.event;

import com.example.oreo.authentication.application.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisteredEventListener {
    private final MailService mailService;

    @Async
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        // nada, se equivocaron aqui XDDD
    }
}
