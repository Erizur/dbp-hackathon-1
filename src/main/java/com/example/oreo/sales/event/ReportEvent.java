package com.example.oreo.sales.event;

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

        private final Instant from;
        private final Instant to;
        private final String branch;
        private final String emailTo;
        private final String requestedBy;

        public ReportEvent(Instant from, Instant to, String branch, String emailTo, String requestedBy) {
                super(from);
                this.from = from;
                this.to = to;
                this.branch = branch;
                this.emailTo = emailTo;
                this.requestedBy = requestedBy;
        }
}