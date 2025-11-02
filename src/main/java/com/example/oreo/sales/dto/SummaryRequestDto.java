package com.example.oreo.sales.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Optional;

@Data
public class SummaryRequestDto {
        private Optional<LocalDate> from;
        private Optional<LocalDate> to;
        @NotEmpty
        private String branch;
        @Email 
        private String emailTo;
}
