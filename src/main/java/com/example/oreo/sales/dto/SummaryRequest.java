package com.example.oreo.sales.dto;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;

public record SummaryRequest(
        LocalDate from,
        LocalDate to,
        String branch,
        @Email String emailTo
) {}
