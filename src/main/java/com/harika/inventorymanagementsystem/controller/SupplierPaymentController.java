package com.harika.inventorymanagementsystem.controller;

import com.harika.inventorymanagementsystem.entity.SupplierPayment;
import com.harika.inventorymanagementsystem.service.SupplierPaymentService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/supplier-payments")
public class SupplierPaymentController {

    private final SupplierPaymentService supplierPaymentService;

    public SupplierPaymentController(
            SupplierPaymentService supplierPaymentService) {

        this.supplierPaymentService = supplierPaymentService;
    }

    // Create Supplier Payment
    @PostMapping
    public SupplierPayment createSupplierPayment(
            @RequestBody SupplierPayment supplierPayment) {

        return supplierPaymentService.saveSupplierPayment(
                supplierPayment
        );
    }

    @GetMapping("/pending/{purchaseInvoiceId}")
    public BigDecimal getPendingAmount(
            @PathVariable Long purchaseInvoiceId) {

        return supplierPaymentService.getPendingAmount(
                purchaseInvoiceId
        );
    }

    // Get all Supplier Payments
    @GetMapping
    public List<SupplierPayment> getAllSupplierPayments() {

        return supplierPaymentService.getAllSupplierPayments();
    }

    // Get Supplier Payment by ID
    @GetMapping("/{id}")
    public Optional<SupplierPayment> getSupplierPaymentById(
            @PathVariable Long id) {

        return supplierPaymentService.getSupplierPaymentById(id);
    }

    // Delete Supplier Payment
    @DeleteMapping("/{id}")
    public void deleteSupplierPayment(
            @PathVariable Long id) {

        supplierPaymentService.deleteSupplierPayment(id);
    }
}