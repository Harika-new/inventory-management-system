package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.entity.PurchaseInvoice;
import com.harika.inventorymanagementsystem.entity.SupplierPayment;
import com.harika.inventorymanagementsystem.exception.InvalidPaymentException;
import com.harika.inventorymanagementsystem.repository.PurchaseInvoiceRepository;
import com.harika.inventorymanagementsystem.repository.SupplierPaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierPaymentService {

    private final SupplierPaymentRepository supplierPaymentRepository;
    private final PurchaseInvoiceRepository purchaseInvoiceRepository;

    public SupplierPaymentService(
            SupplierPaymentRepository supplierPaymentRepository,
            PurchaseInvoiceRepository purchaseInvoiceRepository) {

        this.supplierPaymentRepository = supplierPaymentRepository;
        this.purchaseInvoiceRepository = purchaseInvoiceRepository;
    }

    // Calculate total amount already paid
    // for a particular Purchase Invoice.
    public BigDecimal getTotalPaidForPurchaseInvoice(
            Long purchaseInvoiceId) {

        List<SupplierPayment> payments =
                supplierPaymentRepository
                        .findByPurchaseInvoiceId(purchaseInvoiceId);

        BigDecimal totalPaid = BigDecimal.ZERO;

        for (SupplierPayment payment : payments) {
            totalPaid = totalPaid.add(payment.getAmount());
        }

        return totalPaid;
    }

    // Calculate pending amount for a Purchase Invoice.
    //
    // Pending Amount =
    // Purchase Invoice Total - Total Payments
    public BigDecimal getPendingAmount(Long purchaseInvoiceId) {

        PurchaseInvoice purchaseInvoice =
                purchaseInvoiceRepository
                        .findById(purchaseInvoiceId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Purchase Invoice not found"
                                ));

        BigDecimal totalPaid =
                getTotalPaidForPurchaseInvoice(purchaseInvoiceId);

        return purchaseInvoice.getTotalAmount()
                .subtract(totalPaid);
    }

    // Save Supplier Payment
    public SupplierPayment saveSupplierPayment(
            SupplierPayment supplierPayment) {

        Long purchaseInvoiceId =
                supplierPayment.getPurchaseInvoice().getId();

        // Business rule:
        // Payment amount must be greater than zero.
        if (supplierPayment.getAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidPaymentException(
                    "Payment amount must be greater than zero"
            );
        }

        BigDecimal pendingAmount =
                getPendingAmount(purchaseInvoiceId);

        // Business rule:
        // Payment cannot be greater than pending amount.
        if (supplierPayment.getAmount()
                .compareTo(pendingAmount) > 0) {

            throw new InvalidPaymentException(
                    "Payment cannot be greater than pending amount"
            );
        }

        return supplierPaymentRepository.save(supplierPayment);
    }

    // Get all Supplier Payments
    public List<SupplierPayment> getAllSupplierPayments() {

        return supplierPaymentRepository.findAll();
    }

    // Get Supplier Payment by ID
    public Optional<SupplierPayment> getSupplierPaymentById(
            Long id) {

        return supplierPaymentRepository.findById(id);
    }

    // Delete Supplier Payment by ID
    public void deleteSupplierPayment(Long id) {

        supplierPaymentRepository.deleteById(id);
    }
}