package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.dto.InventorySummaryResponse;
import com.harika.inventorymanagementsystem.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    // Find the current Stock record for a specific Product.
    // Example: Product ID 2 → find its Stock record.
    Optional<Stock> findByProductId(Long productId);

    @Query("""
        SELECT new com.harika.inventorymanagementsystem.dto.InventorySummaryResponse(
            s.product.category,
            s.product.size,
            SUM(s.quantity)
        )
        FROM Stock s
        GROUP BY s.product.category, s.product.size
        ORDER BY s.product.category, s.product.size
        """)
    List<InventorySummaryResponse> getInventorySummary();

}
//query follows
//Stock
//  ↓
//Product
//  ↓
//category + size
//  ↓
//SUM(quantity)
//  ↓
//InventorySummaryResponse

//Stock
//  ↓
//Product
//  ↓
//category + size
//  ↓
//SUM(quantity)
//  ↓
//InventorySummaryResponse
//One important point
//
//This method:
//
//List<InventorySummaryResponse> getInventorySummary();
//
//is not CRUD.
//
//        It's a report/query specifically designed for the dashboard.
//
//And this is exactly why we created the DTO earlier:
//
//DB data
//   ↓
//Custom Repository Query
//   ↓
//InventorySummaryResponse
//   ↓
//Service
//   ↓
//Controller
//   ↓
//Frontend