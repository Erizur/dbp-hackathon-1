package com.example.oreo.sales.event;
import com.example.oreo.sales.domain.Sale;

import java.util.Date;
import java.util.List;
import org.springframework.context.ApplicationEvent;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Getter;

@Getter
public class ReportEvent extends ApplicationEvent {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private String requestId;

        private final Date from;
        private final Date to;
        private final List<Sale> sales;

        @Email
        private final String email;

        public ReportEvent(String email, List<Sale> sales, Date from, Date to) {
                super(sales);
                this.sales = sales;
                this.email = email;
                this.from = from;
                this.to = to;
        }
}