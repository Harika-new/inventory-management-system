package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.dto.CustomerOutstandingResponse;
import com.harika.inventorymanagementsystem.dto.SalesSummaryResponse;
import com.harika.inventorymanagementsystem.entity.SalesInvoice;
import com.harika.inventorymanagementsystem.entity.SalesItem;
import com.harika.inventorymanagementsystem.exception.InvalidDiscountException;
import com.harika.inventorymanagementsystem.repository.SalesInvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SalesInvoiceService {

    private final SalesInvoiceRepository salesInvoiceRepository;
    private final StockService stockService;

    // Constructor injection:
    // Spring creates SalesInvoiceRepository and StockService
    // and injects them here.
    public SalesInvoiceService(
            SalesInvoiceRepository salesInvoiceRepository,
            StockService stockService) {

        this.salesInvoiceRepository = salesInvoiceRepository;
        this.stockService = stockService;
    }

    // Save Sales Invoice
    // Either the complete sale succeeds,
    // or all changes are rolled back.
    @Transactional
    public SalesInvoice saveSalesInvoice(SalesInvoice salesInvoice) {

        // Start invoice total from ₹0.
        BigDecimal total = BigDecimal.ZERO;

        // Process every product on the bill.
        for (SalesItem item : salesInvoice.getSalesItems()) {

            // Connect this SalesItem to its parent SalesInvoice.
            // SalesItem owns the sales_invoice_id foreign key.
            item.setSalesInvoice(salesInvoice);

            // Business rule:
            // Discount cannot be greater than or equal to selling price.
            if (item.getDiscount().compareTo(item.getSellingPrice()) >= 0) {
                throw new InvalidDiscountException(
                        "Discount must be less than selling price"
                );
            }

            // Calculate final price after discount.
            BigDecimal finalPrice =
                    item.getSellingPrice()
                            .subtract(item.getDiscount());

            // Store the calculated final price.
            item.setFinalPrice(finalPrice);

            // Calculate this item's total.
            BigDecimal itemTotal =
                    finalPrice.multiply(
                            BigDecimal.valueOf(item.getQuantity())
                    );

            // Add this item's total to invoice total.
            total = total.add(itemTotal);

            // Decrease stock because the customer bought this quantity.
            stockService.decreaseStock(
                    item.getProduct().getId(),
                    item.getQuantity()
            );
        }

        // Store calculated total in the invoice.
        salesInvoice.setTotalAmount(total);

        // Save the invoice.
        // Cascade will also save SalesItems.
        return salesInvoiceRepository.save(salesInvoice);
    }

    // Get all Sales Invoices
    public List<SalesInvoice> getAllSalesInvoices() {
        return salesInvoiceRepository.findAll();
    }

    // Get Sales Invoice by ID
    public Optional<SalesInvoice> getSalesInvoiceById(Long id) {
        return salesInvoiceRepository.findById(id);
    }

    // Delete Sales Invoice by ID
    public void deleteSalesInvoice(Long id) {
        salesInvoiceRepository.deleteById(id);
    }

    // Get customer-wise outstanding amount.
    public List<CustomerOutstandingResponse> getCustomerOutstanding() {
        return salesInvoiceRepository.getCustomerOutstanding();
    }

    // Get sales dashboard summary.
    public SalesSummaryResponse getSalesSummary() {
        return salesInvoiceRepository.getSalesSummary();
    }
}