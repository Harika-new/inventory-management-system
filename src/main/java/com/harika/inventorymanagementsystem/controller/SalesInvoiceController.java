package com.harika.inventorymanagementsystem.controller;

import com.harika.inventorymanagementsystem.entity.SalesInvoice;
import com.harika.inventorymanagementsystem.service.SalesInvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sales-invoices")
public class SalesInvoiceController {

    private final SalesInvoiceService salesInvoiceService;

    // Constructor injection:
    // Spring injects SalesInvoiceService here.
    public SalesInvoiceController(
            SalesInvoiceService salesInvoiceService) {

        this.salesInvoiceService = salesInvoiceService;
    }

    // Create Sales Invoice
    @PostMapping
    public SalesInvoice createSalesInvoice(
            @RequestBody SalesInvoice salesInvoice) {

        return salesInvoiceService.saveSalesInvoice(salesInvoice);
    }

    // Get all Sales Invoices
    @GetMapping
    public List<SalesInvoice> getAllSalesInvoices() {
        return salesInvoiceService.getAllSalesInvoices();
    }

    // Get Sales Invoice by ID
    @GetMapping("/{id}")
    public Optional<SalesInvoice> getSalesInvoiceById(
            @PathVariable Long id) {

        return salesInvoiceService.getSalesInvoiceById(id);
    }

    // Delete Sales Invoice by ID
    @DeleteMapping("/{id}")
    public void deleteSalesInvoice(@PathVariable Long id) {
        salesInvoiceService.deleteSalesInvoice(id);
    }
}