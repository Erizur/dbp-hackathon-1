package com.example.oreo.sales.dto;     

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public record SalesCreateDto(
        @NotBlank String sku,
        @Min(1) int units,
        @DecimalMin("0.0") BigDecimal price,
        @NotBlank String branch,
        @NotNull Instant soldAt
) {}
