package com.harika.inventorymanagementsystem.controller;

import com.harika.inventorymanagementsystem.entity.PurchaseInvoice;
import com.harika.inventorymanagementsystem.service.PurchaseInvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/purchase-invoices")
public class PurchaseInvoiceController {

    private final PurchaseInvoiceService purchaseInvoiceService;

    public PurchaseInvoiceController(PurchaseInvoiceService purchaseInvoiceService) {
        this.purchaseInvoiceService = purchaseInvoiceService;
    }

    @PostMapping
    public PurchaseInvoice createPurchaseInvoice(@RequestBody PurchaseInvoice purchaseInvoice) {
        return purchaseInvoiceService.savePurchaseInvoice(purchaseInvoice);
    }

    @GetMapping
    public List<PurchaseInvoice> getAllPurchaseInvoices() {
        return purchaseInvoiceService.getAllPurchaseInvoices();
    }

    @GetMapping("/{id}")
    public Optional<PurchaseInvoice> getPurchaseInvoiceById(@PathVariable Long id) {
        return purchaseInvoiceService.getPurchaseInvoiceById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePurchaseInvoice(@PathVariable Long id) {
        purchaseInvoiceService.deletePurchaseInvoice(id);
    }
}