package com.harika.inventorymanagementsystem.controller;

import com.harika.inventorymanagementsystem.entity.PurchaseItem;
import com.harika.inventorymanagementsystem.service.PurchaseItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/purchase-items")
public class PurchaseItemController {

    private final PurchaseItemService purchaseItemService;

    public PurchaseItemController(PurchaseItemService purchaseItemService) {
        this.purchaseItemService = purchaseItemService;
    }

    @PostMapping
    public PurchaseItem createPurchaseItem(@RequestBody PurchaseItem purchaseItem) {
        return purchaseItemService.savePurchaseItem(purchaseItem);
    }

    @GetMapping
    public List<PurchaseItem> getAllPurchaseItems() {
        return purchaseItemService.getAllPurchaseItems();
    }

    @GetMapping("/{id}")
    public Optional<PurchaseItem> getPurchaseItemById(@PathVariable Long id) {
        return purchaseItemService.getPurchaseItemById(id);
    }

    @DeleteMapping("/{id}")
    public void deletePurchaseItem(@PathVariable Long id) {
        purchaseItemService.deletePurchaseItem(id);
    }
}