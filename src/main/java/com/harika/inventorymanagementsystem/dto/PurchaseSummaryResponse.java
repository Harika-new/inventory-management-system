package com.harika.inventorymanagementsystem.dto;

import java.math.BigDecimal;

public class PurchaseSummaryResponse {

    private Long totalInvoices;
    private Long totalUnitsPurchased;
    private BigDecimal totalPurchaseAmount;

    public PurchaseSummaryResponse(
            Long totalInvoices,
            Long totalUnitsPurchased,
            BigDecimal totalPurchaseAmount) {

        this.totalInvoices = totalInvoices;
        this.totalUnitsPurchased = totalUnitsPurchased;
        this.totalPurchaseAmount = totalPurchaseAmount;
    }

    public Long getTotalInvoices() {
        return totalInvoices;
    }

    public Long getTotalUnitsPurchased() {
        return totalUnitsPurchased;
    }

    public BigDecimal getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }
}