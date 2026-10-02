package com.harika.inventorymanagementsystem.dto;

public class SalesSummaryResponse {

    private Long totalBills;
    private Long totalUnitsSold;
    private java.math.BigDecimal totalSalesAmount;

    public SalesSummaryResponse(Long totalBills,
                                Long totalUnitsSold,
                                java.math.BigDecimal totalSalesAmount) {
        this.totalBills = totalBills;
        this.totalUnitsSold = totalUnitsSold;
        this.totalSalesAmount = totalSalesAmount;
    }

    public Long getTotalBills() {
        return totalBills;
    }

    public Long getTotalUnitsSold() {
        return totalUnitsSold;
    }

    public java.math.BigDecimal getTotalSalesAmount() {
        return totalSalesAmount;
    }
}