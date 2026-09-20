package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.entity.PurchaseInvoice;
import com.harika.inventorymanagementsystem.entity.PurchaseItem;
import com.harika.inventorymanagementsystem.repository.PurchaseInvoiceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseInvoiceService {

    private final PurchaseInvoiceRepository purchaseInvoiceRepository;
    private final StockService stockService;

    // Constructor injection:
    // Spring creates both dependencies and injects them here.
    public PurchaseInvoiceService(
            PurchaseInvoiceRepository purchaseInvoiceRepository,
            StockService stockService) {

        this.purchaseInvoiceRepository = purchaseInvoiceRepository;
        this.stockService = stockService;
    }

    // Save Purchase Invoice
    public PurchaseInvoice savePurchaseInvoice(PurchaseInvoice purchaseInvoice) {

        // Connect each PurchaseItem to its parent PurchaseInvoice.
        // PurchaseItem owns the purchase_invoice_id foreign key.
        for (PurchaseItem item : purchaseInvoice.getPurchaseItems()) {
            item.setPurchaseInvoice(purchaseInvoice);
        }

        // Start invoice total from ₹0.
        BigDecimal total = BigDecimal.ZERO;

        // Get all PurchaseItems belonging to this invoice
        // and process each item one by one.
        for (PurchaseItem item : purchaseInvoice.getPurchaseItems()) {

            // Calculate this item's amount:
            // quantity × purchase price
            BigDecimal itemAmount =
                    item.getPurchasePrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

            // Add the current item's amount to the invoice total.
            // Example:
            // Item 1 = ₹3,000 → total = ₹3,000
            // Item 2 = ₹2,500 → total = ₹5,500
            total = total.add(itemAmount);

            // Increase Stock using only the quantity actually received
            // and accepted by the shop.
            //
            // Example:
            // Invoice quantity = 10
            // Received quantity = 8
            // Stock increases by 8, not 10.
            stockService.increaseStock(
                    item.getProduct().getId(),
                    item.getReceivedQuantity()
            );
        }

        // Put the calculated total into the PurchaseInvoice object.
        purchaseInvoice.setTotalAmount(total);

        // Save the invoice.
        // Cascade will also save its PurchaseItems.
        return purchaseInvoiceRepository.save(purchaseInvoice);
    }

    // Get all Purchase Invoices
    public List<PurchaseInvoice> getAllPurchaseInvoices() {
        return purchaseInvoiceRepository.findAll();
    }

    // Get one Purchase Invoice by ID
    public Optional<PurchaseInvoice> getPurchaseInvoiceById(Long id) {
        return purchaseInvoiceRepository.findById(id);
    }

    // Delete Purchase Invoice by ID
    public void deletePurchaseInvoice(Long id) {
        purchaseInvoiceRepository.deleteById(id);
    }
}