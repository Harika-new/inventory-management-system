package com.harika.inventorymanagementsystem.dto;

public class InventorySummaryResponse {

    private String category;
    private String size;
    private Long totalPieces;

    public InventorySummaryResponse(
            String category,
            String size,
            Long totalPieces) {
        this.category = category;
        this.size = size;
        this.totalPieces = totalPieces;
    }

    public String getCategory() {
        return category;
    }

    public String getSize() {
        return size;
    }

    public Long getTotalPieces() {
        return totalPieces;
    }
}