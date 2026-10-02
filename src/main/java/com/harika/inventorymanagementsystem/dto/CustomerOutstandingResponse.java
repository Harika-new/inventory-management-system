package com.harika.inventorymanagementsystem.dto;

import java.math.BigDecimal;

public class CustomerOutstandingResponse {

    private String customerName;
    private BigDecimal outstandingAmount;

    public CustomerOutstandingResponse(
            String customerName,
            BigDecimal outstandingAmount) {

        this.customerName = customerName;
        this.outstandingAmount = outstandingAmount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }
}