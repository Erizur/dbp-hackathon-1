package com.example.oreo.sales.repository;


import com.example.oreo.sales.domain.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, String> {

    @Query("""
           SELECT s FROM Sale s
           WHERE s.soldAt BETWEEN :from AND :to
           AND (:branch IS NULL OR LOWER(s.branch) = LOWER(:branch))
           """)
    List<Sale> findByDateRangeAndBranch(Instant from, Instant to, String branch);
    Page<Sale> findAllBySoldAtBetween(Instant from, Instant to, Pageable pageable);
    Page<Sale> findAllByBranchAndSoldAtBetween(String branch, Instant from, Instant to, Pageable pageable);
}
