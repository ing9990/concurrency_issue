package com.concurrency_issue.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Product, Long> {

    @Query("SELECT t FROM Product t WHERE t.productId = ?1")
    Optional<Product> findByProductId(Long id);
}
