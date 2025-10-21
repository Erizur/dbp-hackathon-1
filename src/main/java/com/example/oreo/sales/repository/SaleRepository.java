package com.example.oreo.sales.repository;

import com.example.oreo.sales.domain.Sale;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, String> {

    @Query("SELECT s FROM Sale s WHERE s.soldAt BETWEEN :from AND :to " +
           "AND (:branch IS NULL OR s.branch = :branch)")
    List<Sale> findByDateRangeAndBranch(@Param("from") Instant from,
                                        @Param("to") Instant to,
                                        @Param("branch") String branch);

    @Query("SELECT s FROM Sale s WHERE s.soldAt BETWEEN :from AND :to")
    List<Sale> findByDateRange(@Param("from") Instant from,
                               @Param("to") Instant to);
}
