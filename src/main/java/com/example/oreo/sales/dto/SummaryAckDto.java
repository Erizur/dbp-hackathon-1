package com.example.oreo.sales.dto;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SummaryAckDto {
        @NotEmpty
        String requestId;
        @NotEmpty
        String status;
        @NotEmpty
        String message;
        @NotEmpty
        String estimatedTime;
        @NotNull
        Instant requestedAt;
}
