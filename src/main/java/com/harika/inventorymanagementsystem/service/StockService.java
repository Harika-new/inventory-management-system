
package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.dto.InventorySummaryResponse;
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

        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException(
                    "Product ID must be greater than zero"
            );
        }

        if (receivedQuantity == null || receivedQuantity <= 0) {
            throw new IllegalArgumentException(
                    "Received quantity must be greater than zero"
            );
        }

        Optional<Stock> existingStock =
                stockRepository.findByProductId(productId);

        if (existingStock.isPresent()) {
            Stock stock = existingStock.get();

            if (stock.getQuantity() == null || stock.getQuantity() < 0) {
                throw new IllegalStateException(
                        "Stock quantity is invalid"
                );
            }

            stock.setQuantity(
                    stock.getQuantity() + receivedQuantity
            );

            return stockRepository.save(stock);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(
                        () -> new RuntimeException("Product not found")
                );

        Stock newStock = new Stock();
        newStock.setProduct(product);
        newStock.setQuantity(receivedQuantity);

        return stockRepository.save(newStock);
    }

    // Decrease available stock when a product is sold.
    public Stock decreaseStock(Long productId, Integer soldQuantity) {

        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException(
                    "Product ID must be greater than zero"
            );
        }

        if (soldQuantity == null || soldQuantity <= 0) {
            throw new IllegalArgumentException(
                    "Sold quantity must be greater than zero"
            );
        }

        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(
                        () -> new RuntimeException("Stock not found")
                );

        if (stock.getQuantity() == null || stock.getQuantity() < 0) {
            throw new IllegalStateException(
                    "Stock quantity is invalid"
            );
        }

        if (stock.getQuantity() < soldQuantity) {
            throw new InsufficientStockException(
                    "Insufficient stock. Available: "
                            + stock.getQuantity()
                            + ", Requested: "
                            + soldQuantity
            );
        }

        stock.setQuantity(
                stock.getQuantity() - soldQuantity
        );

        return stockRepository.save(stock);
    }

    // Get category-wise and size-wise inventory summary.
    public List<InventorySummaryResponse> getInventorySummary() {
        return stockRepository.getInventorySummary();
    }
}
