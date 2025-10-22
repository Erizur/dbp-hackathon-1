package com.example.oreo.sales.dto;

import java.time.Instant;

import lombok.Data;

@Data
public class ReportEventDto {
        String requestId;
        Instant from;
        Instant to;
        String branch;
        String emailTo;
        String requestedBy;
}