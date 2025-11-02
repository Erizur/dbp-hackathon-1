package com.example.oreo.authentication.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private Long expiresIn;
    private String email;
    private String role;
}
