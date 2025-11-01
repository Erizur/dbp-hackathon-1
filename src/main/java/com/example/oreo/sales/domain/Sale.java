package com.example.oreo.sales.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(indexes = {
    @Index(columnList = "branch"),
    @Index(columnList = "soldAt")
})
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private int units;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String branch;

    @Column(nullable = false)
    private Instant soldAt;

    private String createdBy;
}

