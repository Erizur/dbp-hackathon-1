package com.example.oreo.sales.dto;     

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Data;

@Data
public class SalesResponseDto(
        String id,
        String sku,
        int units,
        BigDecimal price,
        String branch,
        Instant soldAt,
        String createdBy
) {}
