package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    // Find the current Stock record for a specific Product.
    // Example: Product ID 2 → find its Stock record.
    Optional<Stock> findByProductId(Long productId);

}