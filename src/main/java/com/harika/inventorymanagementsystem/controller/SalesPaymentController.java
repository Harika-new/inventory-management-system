package com.harika.inventorymanagementsystem.controller;

import com.harika.inventorymanagementsystem.entity.SalesPayment;
import com.harika.inventorymanagementsystem.service.SalesPaymentService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sales-payments")
public class SalesPaymentController {

    private final SalesPaymentService salesPaymentService;

    public SalesPaymentController(
            SalesPaymentService salesPaymentService) {
        this.salesPaymentService = salesPaymentService;
    }

    @PostMapping
    public SalesPayment createSalesPayment(
            @RequestBody SalesPayment salesPayment) {

        return salesPaymentService.saveSalesPayment(salesPayment);
    }

    @GetMapping("/pending/{salesInvoiceId}")
    public BigDecimal getPendingAmount(
            @PathVariable Long salesInvoiceId) {

        return salesPaymentService.getPendingAmount(salesInvoiceId);
    }

    @GetMapping
    public List<SalesPayment> getAllSalesPayments() {

        return salesPaymentService.getAllSalesPayments();
    }

    @GetMapping("/{id}")
    public Optional<SalesPayment> getSalesPaymentById(
            @PathVariable Long id) {

        return salesPaymentService.getSalesPaymentById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSalesPayment(
            @PathVariable Long id) {

        salesPaymentService.deleteSalesPayment(id);
    }
}