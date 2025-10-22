package com.example.oreo.user.dto;

import jakarta.validation.constraints.*;

public record RegisterUserDto(
        @NotBlank @Pattern(regexp = "^[A-Za-z0-9_.]{3,30}$")
        String username,

        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank
        String role,

        String branch
) {}
