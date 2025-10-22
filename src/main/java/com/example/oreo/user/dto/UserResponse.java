package com.example.oreo.user.dto;


import java.time.Instant;

public record UserResponse(
        String id,
        String username,
        String email,
        String role,
        String branch,
        Instant createdAt
) {}
