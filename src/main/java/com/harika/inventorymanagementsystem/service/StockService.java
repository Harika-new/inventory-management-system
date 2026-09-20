package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.entity.Product;
import com.harika.inventorymanagementsystem.entity.Stock;
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

    // Increase available stock using the quantity actually received.
    public Stock increaseStock(Long productId, Integer receivedQuantity) {

        // Try to find the existing Stock record for this Product.
        Optional<Stock> existingStock =
                stockRepository.findByProductId(productId);

        if (existingStock.isPresent()) {

            // Stock already exists for this Product.
            Stock stock = existingStock.get();

            // Add the newly received quantity
            // to the current available quantity.
            stock.setQuantity(
                    stock.getQuantity() + receivedQuantity
            );

            // Save the updated Stock record.
            return stockRepository.save(stock);

        } else {

            // No Stock record exists yet.
            // This means this is the first purchase of this Product.
            Stock newStock = new Stock();

            // Find the Product using its ID.
            Product product = productRepository.findById(productId)
                    .orElseThrow(
                            () -> new RuntimeException("Product not found")
                    );

            // Connect the new Stock record to the Product.
            newStock.setProduct(product);

            // Since this is the first Stock record,
            // receivedQuantity becomes the initial available stock.
            newStock.setQuantity(receivedQuantity);

            // Save the new Stock record.
            return stockRepository.save(newStock);
        }
    }
}