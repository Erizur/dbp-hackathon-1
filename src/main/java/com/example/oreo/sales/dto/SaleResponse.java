package com.example.oreo.sales.dto;     

import java.math.BigDecimal;
import java.time.Instant;

public record SaleResponse(
        String id,
        String sku,
        int units,
        BigDecimal price,
        String branch,
        Instant soldAt,
        String createdBy
) {}
