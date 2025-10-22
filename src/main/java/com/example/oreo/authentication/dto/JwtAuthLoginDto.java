package com.example.oreo.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtAuthLoginDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
