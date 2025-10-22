package com.example.oreo.sales.dto;     

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class SalesCreateDto {
        @NotBlank 
        private String sku;
        
        @Min(1) 
        private int units;
        @DecimalMin("0.0") 
        private BigDecimal price;
        @NotBlank 
        private String branch;
        @NotNull 
        private Instant soldAt;
}