package com.example.oreo.user.dto;

import com.example.oreo.user.domain.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterUserDto {
    @NotBlank
    private String username;
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Role role;
}
