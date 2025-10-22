package com.example.oreo.sales.dto;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Data;

@Data
public class SalesResponseDto {
        private String id;
        private String sku;
        private int units;
        private BigDecimal price;
        private String branch;
        private Instant soldAt;
        private String createdBy;
}
