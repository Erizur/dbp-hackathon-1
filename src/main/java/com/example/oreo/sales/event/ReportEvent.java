package com.example.oreo.sales.event;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.context.ApplicationEvent;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
public class ReportEvent extends ApplicationEvent {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private String requestId;

        private final Integer totalUnits;
        private final BigDecimal totalRevenue;
        private final String topSku;
        private final String topBranch;

        public ReportEvent(Integer totalUnits, BigDecimal totalRevenue, String topSku, String topBranch) {
                super(totalUnits);
                this.totalUnits = totalUnits;
                this.totalRevenue = totalRevenue;
                this.topSku = topSku;
                this.topBranch = topBranch;
        }
}