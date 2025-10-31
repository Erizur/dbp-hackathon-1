package com.example.oreo.sales.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SummaryRequestDto {
        @NotNull
        private LocalDate from;
        @NotNull
        private LocalDate to;
        @NotEmpty
        private String branch;
        @Email 
        private String emailTo;
}
