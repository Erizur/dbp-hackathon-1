package com.example.oreo.sales.dto;

import java.time.Instant;

public record SummaryAck(
        String requestId,
        String status,
        String message,
        String estimatedTime,
        Instant requestedAt
) {}
