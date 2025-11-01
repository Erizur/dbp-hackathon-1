package com.example.oreo.sales.event;

import java.util.List;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.oreo.mail.EmailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReportListener {

    private final RestTemplate restTemplate;
    private final EmailService emailService;

    @EventListener
    @Async
    public void handleReportEvent(ReportEvent event) {
        String url = System.getenv("GITHUB_MODELS_URL");

        Map<String, Object> body = Map.of(
            "model", System.getenv("MODEL_ID"),
            "messages", List.of(
                Map.of("role", "system", "content",
                    "Eres un analista que escribe resúmenes breves y claros para emails corporativos."),
                Map.of("role", "user", "content",
                    "Con estos datos: totalUnits=" + event.getTotalUnits() +
                    ", totalRevenue=" + event.getTotalRevenue() +
                    ", topSku=" + event.getTopSku() +
                    ", topBranch=" + event.getTopBranch() +
                    ". Devuelve un resumen ≤120 palabras para enviar por email.")
            ),
            "max_tokens", 200
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(System.getenv("GITHUB_TOKEN"));
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
            restTemplate.postForEntity(url, entity, String.class);

        response.getBody();
    }
}
