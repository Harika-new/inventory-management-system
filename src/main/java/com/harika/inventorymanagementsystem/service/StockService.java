package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.entity.Product;
import com.harika.inventorymanagementsystem.entity.Stock;
import com.harika.inventorymanagementsystem.exception.InsufficientStockException;
import com.harika.inventorymanagementsystem.repository.ProductRepository;
import com.harika.inventorymanagementsystem.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    // Constructor injection:
    // Spring creates both repositories and injects them here.
    public StockService(
            StockRepository stockRepository,
            ProductRepository productRepository) {

        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    // Save Stock
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    // Get all Stock records
    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    // Get Stock by ID
    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    // Delete Stock by ID
    public void deleteStock(Long id) {
        stockRepository.deleteById(id);
    }

    // Increase available stock when products are received.
    public Stock increaseStock(Long productId, Integer receivedQuantity) {

        // Check whether Stock already exists for this Product.
        Optional<Stock> existingStock =
                stockRepository.findByProductId(productId);

        if (existingStock.isPresent()) {

            // Existing Stock record found.
            Stock stock = existingStock.get();

            // Add newly received quantity to current stock.
            stock.setQuantity(
                    stock.getQuantity() + receivedQuantity
            );

            // Save updated Stock.
            return stockRepository.save(stock);

        } else {

            // No Stock record exists.
            // This is the first purchase of this Product.
            Stock newStock = new Stock();

            // Find the Product using its ID.
            Product product = productRepository.findById(productId)
                    .orElseThrow(
                            () -> new RuntimeException("Product not found")
                    );

            // Connect Stock to Product.
            newStock.setProduct(product);

            // Set initial Stock quantity.
            newStock.setQuantity(receivedQuantity);

            // Save new Stock record.
            return stockRepository.save(newStock);
        }
    }

    // Decrease available stock when a product is sold.
    public Stock decreaseStock(Long productId, Integer soldQuantity) {

        // Find the existing Stock record for this Product.
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(
                        () -> new RuntimeException("Stock not found")
                );
        if (stock.getQuantity() < soldQuantity) {
            throw new InsufficientStockException(
                    "Insufficient stock. Available: "
                            + stock.getQuantity()
                            + ", Requested: "
                            + soldQuantity
            );
        }

        // Reduce available stock.
        stock.setQuantity(
                stock.getQuantity() - soldQuantity
        );

        // Save and return updated Stock.
        return stockRepository.save(stock);
    }
}