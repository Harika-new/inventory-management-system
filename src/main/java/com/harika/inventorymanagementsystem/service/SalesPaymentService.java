package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.entity.SalesInvoice;
import com.harika.inventorymanagementsystem.entity.SalesPayment;
import com.harika.inventorymanagementsystem.exception.InvalidPaymentException;
import com.harika.inventorymanagementsystem.exception.SalesInvoiceNotFoundException;
import com.harika.inventorymanagementsystem.repository.SalesInvoiceRepository;
import com.harika.inventorymanagementsystem.repository.SalesPaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SalesPaymentService {

    private final SalesPaymentRepository salesPaymentRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;

    public SalesPaymentService(
            SalesPaymentRepository salesPaymentRepository,
            SalesInvoiceRepository salesInvoiceRepository) {

        this.salesPaymentRepository = salesPaymentRepository;
        this.salesInvoiceRepository = salesInvoiceRepository;
    }

    // Calculate total amount already paid
    // for a particular Sales Invoice.
    public BigDecimal getTotalPaidForSalesInvoice(
            Long salesInvoiceId) {

        List<SalesPayment> payments =
                salesPaymentRepository
                        .findBySalesInvoiceId(salesInvoiceId);

        BigDecimal totalPaid = BigDecimal.ZERO;

        for (SalesPayment payment : payments) {
            totalPaid = totalPaid.add(payment.getAmount());
        }

        return totalPaid;
    }

    // Calculate pending amount for a Sales Invoice.
    //
    // Pending Amount =
    // Sales Invoice Total - Total Payments
    public BigDecimal getPendingAmount(Long salesInvoiceId) {

        SalesInvoice salesInvoice =
                salesInvoiceRepository
                        .findById(salesInvoiceId)
                        .orElseThrow(() ->
                                new SalesInvoiceNotFoundException(
                                        "Sales Invoice not found"
                                ));

        BigDecimal totalPaid =
                getTotalPaidForSalesInvoice(salesInvoiceId);

        return salesInvoice.getTotalAmount()
                .subtract(totalPaid);
    }

    // Save Sales Payment
    public SalesPayment saveSalesPayment(
            SalesPayment salesPayment) {

        Long salesInvoiceId =
                salesPayment.getSalesInvoice().getId();

        // Business rule:
        // Payment amount must be greater than zero.
        if (salesPayment.getAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidPaymentException(
                    "Payment amount must be greater than zero"
            );
        }

        // Business rule:
        // Payment date cannot be in the future.
        if (salesPayment.getPaymentDate()
                .isAfter(LocalDate.now())) {

            throw new InvalidPaymentException(
                    "Payment date cannot be in the future"
            );
        }

        BigDecimal pendingAmount =
                getPendingAmount(salesInvoiceId);

        // Business rule:
        // Payment cannot be greater than pending amount.
        if (salesPayment.getAmount()
                .compareTo(pendingAmount) > 0) {

            throw new InvalidPaymentException(
                    "Payment cannot be greater than pending amount"
            );
        }

        return salesPaymentRepository.save(salesPayment);
    }

    // Get all Sales Payments
    public List<SalesPayment> getAllSalesPayments() {

        return salesPaymentRepository.findAll();
    }

    // Get Sales Payment by ID
    public Optional<SalesPayment> getSalesPaymentById(
            Long id) {

        return salesPaymentRepository.findById(id);
    }

    // Delete Sales Payment by ID
    public void deleteSalesPayment(Long id) {

        salesPaymentRepository.deleteById(id);
    }
}