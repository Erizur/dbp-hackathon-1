package com.example.oreo.user.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String branch;

    private Instant createdAt = Instant.now();

    // Getters y setters
    // (puedes generar con Lombok si está permitido)
}
