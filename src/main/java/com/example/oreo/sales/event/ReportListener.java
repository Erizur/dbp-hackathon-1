package com.example.oreo.sales.event;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.oreo.authentication.application.MailService;
import com.example.oreo.sales.domain.Sale;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReportListener {

    private final RestTemplate restTemplate;
    private final MailService mailService;

    @EventListener
    @Async
    public void handleReportEvent(ReportEvent event) throws JsonMappingException, JsonProcessingException {
        String url = System.getenv("GITHUB_MODELS_URL");
        List<Sale> sales = event.getSales();

        int totalUnits = sales.stream()
                .mapToInt(Sale::getUnits)
                .sum();

        BigDecimal totalRevenue = sales.stream()
                .map(s -> s.getPrice().multiply(BigDecimal.valueOf(s.getUnits())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String topSku = sales.stream()
                .collect(Collectors.groupingBy(Sale::getSku,
                        Collectors.summingInt(Sale::getUnits)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        String topBranch = sales.stream()
                .collect(Collectors.groupingBy(Sale::getBranch,
                        Collectors.reducing(BigDecimal.ZERO,
                                s -> s.getPrice().multiply(BigDecimal.valueOf(s.getUnits())),
                                BigDecimal::add)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        Map<String, Object> body = Map.of(
            "model", System.getenv("MODEL_ID"),
            "messages", List.of(
                Map.of("role", "system", "content",
                    "Eres un analista que escribe resúmenes breves y claros para emails corporativos."),
                Map.of("role", "user", "content",
                    "Con estos datos: totalUnits=" + totalUnits +
                    ", totalRevenue=" + totalRevenue +
                    ", topSku=" + topSku +
                    ", topBranch=" + topBranch +
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
            
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String summary = root
                .path("choices").get(0)
                .path("message")
                .path("content")
                .asText();

            mailService.resultMail(event.getEmail(), summary, event.getFrom(), event.getTo());
        }
    }
}
