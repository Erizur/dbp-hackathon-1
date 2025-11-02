package com.example.oreo.sales.repository;

import com.example.oreo.sales.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface SaleRepository extends JpaRepository<Sale, String> {

    Page<Sale> findAllBySoldAtBetween(Instant from, Instant to, Pageable pageable);
    Page<Sale> findAllByBranchIgnoreCaseAndSoldAtBetween(String branch, Instant from, Instant to, Pageable pageable);
    Page<Sale> findAllByBranchAndSoldAtBetween(String branch, Instant from, Instant to, Pageable pageable);
}
