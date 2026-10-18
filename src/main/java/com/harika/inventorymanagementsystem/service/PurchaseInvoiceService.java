
package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.dto.PurchaseSummaryResponse;
import com.harika.inventorymanagementsystem.dto.SupplierOutstandingResponse;
import com.harika.inventorymanagementsystem.entity.PurchaseInvoice;
import com.harika.inventorymanagementsystem.entity.PurchaseItem;
import com.harika.inventorymanagementsystem.repository.PurchaseInvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseInvoiceService {

    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final StockService stockService;

    public PurchaseInvoiceService(
            PurchaseInvoiceRepository purchaseInvoiceRepository,
            StockService stockService) {
        this.purchaseInvoiceRepository = purchaseInvoiceRepository;
        this.stockService = stockService;
    }

    // Save Purchase Invoice
    @Transactional
    public PurchaseInvoice savePurchaseInvoice(
            PurchaseInvoice purchaseInvoice) {

        if (purchaseInvoice == null
                || purchaseInvoice.getPurchaseItems() == null
                || purchaseInvoice.getPurchaseItems().isEmpty()) {
            throw new IllegalArgumentException(
                    "Purchase invoice must contain at least one item"
            );
        }

        // Validate all items before updating stock.
        for (PurchaseItem item : purchaseInvoice.getPurchaseItems()) {
            if (item == null
                    || item.getProduct() == null
                    || item.getProduct().getId() == null) {
                throw new IllegalArgumentException(
                        "Each purchase item must have a valid product"
                );
            }

            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException(
                        "Ordered quantity must be greater than zero"
                );
            }

            if (item.getPurchasePrice() == null
                    || item.getPurchasePrice().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(
                        "Purchase price cannot be null or negative"
                );
            }

            if (item.getReceivedQuantity() == null
                    || item.getReceivedQuantity() < 0) {
                throw new IllegalArgumentException(
                        "Received quantity cannot be null or negative"
                );
            }

            if (item.getReceivedQuantity() > item.getQuantity()) {
                throw new IllegalArgumentException(
                        "Received quantity cannot exceed ordered quantity"
                );
            }
        }

        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseItem item : purchaseInvoice.getPurchaseItems()) {
            item.setPurchaseInvoice(purchaseInvoice);

            BigDecimal itemAmount = item.getPurchasePrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(itemAmount);

            // Increase stock only for pieces actually received.
            if (item.getReceivedQuantity() > 0) {
                stockService.increaseStock(
                        item.getProduct().getId(),
                        item.getReceivedQuantity()
                );
            }
        }

        purchaseInvoice.setTotalAmount(total);

        return purchaseInvoiceRepository.save(purchaseInvoice);
    }

    // Get supplier-wise outstanding amount.
    public List<SupplierOutstandingResponse> getSupplierOutstanding() {
        return purchaseInvoiceRepository.getSupplierOutstanding();
    }

    // Get purchase dashboard summary.
    public PurchaseSummaryResponse getPurchaseSummary() {
        Long totalInvoices =
                purchaseInvoiceRepository.getTotalPurchaseInvoiceCount();

        BigDecimal totalPurchaseAmount =
                purchaseInvoiceRepository.getTotalPurchaseInvoiceAmount();

        Long totalUnitsPurchased =
                purchaseInvoiceRepository.getTotalUnitsPurchased();

        return new PurchaseSummaryResponse(
                totalInvoices,
                totalUnitsPurchased,
                totalPurchaseAmount
        );
    }

    // Get all Purchase Invoices.
    public List<PurchaseInvoice> getAllPurchaseInvoices() {
        return purchaseInvoiceRepository.findAll();
    }

    // Get one Purchase Invoice by ID.
    public Optional<PurchaseInvoice> getPurchaseInvoiceById(Long id) {
        return purchaseInvoiceRepository.findById(id);
    }

    // Delete Purchase Invoice by ID.
    public void deletePurchaseInvoice(Long id) {
        purchaseInvoiceRepository.deleteById(id);
    }
}
