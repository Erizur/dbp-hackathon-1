package com.example.oreo.sales.dto;

import java.time.Instant;

public record ReportRequestedEvent(
        String requestId,
        Instant from,
        Instant to,
        String branch,
        String emailTo,
        String requestedBy
) {}
