package com.harika.inventorymanagementsystem.controller;

import com.harika.inventorymanagementsystem.entity.Stock;
import com.harika.inventorymanagementsystem.service.StockService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockService stockService;

    // Constructor injection:
    // Spring creates StockService and injects it here.
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    // POST /stocks
    // Creates a Stock record for a Product.
    @PostMapping
    public Stock createStock(@RequestBody Stock stock) {
        return stockService.saveStock(stock);
    }

    // GET /stocks
    // Returns all Stock records.
    @GetMapping
    public List<Stock> getAllStock() {
        return stockService.getAllStock();
    }

    // GET /stocks/{id}
    // Returns one Stock record by its ID.
    @GetMapping("/{id}")
    public Optional<Stock> getStockById(@PathVariable Long id) {
        return stockService.getStockById(id);
    }

    // DELETE /stocks/{id}
    // Deletes a Stock record by its ID.
    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
    }
}