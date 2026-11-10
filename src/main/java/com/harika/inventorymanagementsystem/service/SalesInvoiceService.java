
package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.dto.CustomerOutstandingResponse;
import com.harika.inventorymanagementsystem.dto.SalesSummaryResponse;
import com.harika.inventorymanagementsystem.entity.SalesInvoice;
import com.harika.inventorymanagementsystem.entity.SalesItem;
import com.harika.inventorymanagementsystem.exception.InvalidDiscountException;
import com.harika.inventorymanagementsystem.repository.SalesInvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SalesInvoiceService {

    private final SalesInvoiceRepository salesInvoiceRepository;
    private final StockService stockService;

    // Constructor injection
    public SalesInvoiceService(
            SalesInvoiceRepository salesInvoiceRepository,
            StockService stockService) {

        this.salesInvoiceRepository = salesInvoiceRepository;
        this.stockService = stockService;
    }

    // Save Sales Invoice
    // Complete sale succeeds or all changes are rolled back.
    @Transactional
    public SalesInvoice saveSalesInvoice(SalesInvoice salesInvoice) {

        BigDecimal total = BigDecimal.ZERO;

        for (SalesItem item : salesInvoice.getSalesItems()) {

            // Connect SalesItem to SalesInvoice.
            item.setSalesInvoice(salesInvoice);

            // Validate discount.
            if (item.getDiscount().compareTo(item.getSellingPrice()) >= 0) {
                throw new InvalidDiscountException(
                        "Discount must be less than selling price"
                );
            }

            // Calculate final price after discount.
            BigDecimal finalPrice =
                    item.getSellingPrice().subtract(item.getDiscount());

            item.setFinalPrice(finalPrice);

            // Calculate item total.
            BigDecimal itemTotal =
                    finalPrice.multiply(
                            BigDecimal.valueOf(item.getQuantity())
                    );

            total = total.add(itemTotal);

            // Reduce available stock.
            stockService.decreaseStock(
                    item.getProduct().getId(),
                    item.getQuantity()
            );
        }

        // Set calculated invoice total.
        salesInvoice.setTotalAmount(total);

        // Cascade saves SalesItems.
        return salesInvoiceRepository.save(salesInvoice);
    }

    // Get all Sales Invoices.
    public List<SalesInvoice> getAllSalesInvoices() {
        return salesInvoiceRepository.findAll();
    }

    // Get Sales Invoice by ID.
    public Optional<SalesInvoice> getSalesInvoiceById(Long id) {
        return salesInvoiceRepository.findById(id);
    }

    public void deleteSalesInvoice(Long id) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Sales invoices cannot be deleted. Financial history must be preserved."
        );
    }
    // Get customer-wise outstanding amount.
    public List<CustomerOutstandingResponse> getCustomerOutstanding() {
        return salesInvoiceRepository.getCustomerOutstanding();
    }

    // Get sales dashboard summary.
    public SalesSummaryResponse getSalesSummary() {

        Long totalBills =
                salesInvoiceRepository.countTotalBills();

        Long totalUnitsSold =
                salesInvoiceRepository.getTotalUnitsSold();

        BigDecimal totalSalesAmount =
                salesInvoiceRepository.getTotalSalesAmount();

        return new SalesSummaryResponse(
                totalBills,
                totalUnitsSold,
                totalSalesAmount
        );
    }
}
