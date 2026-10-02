package com.harika.inventorymanagementsystem.dto;

import java.math.BigDecimal;

public class SupplierOutstandingResponse {

    private String supplierName;
    private BigDecimal outstandingAmount;

    public SupplierOutstandingResponse(
            String supplierName,
            BigDecimal outstandingAmount) {

        this.supplierName = supplierName;
        this.outstandingAmount = outstandingAmount;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }
}