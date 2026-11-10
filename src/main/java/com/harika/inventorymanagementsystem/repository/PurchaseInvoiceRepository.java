package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.dto.SupplierOutstandingResponse;
import com.harika.inventorymanagementsystem.entity.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PurchaseInvoiceRepository
        extends JpaRepository<PurchaseInvoice, Long> {

    // Get supplier-wise outstanding amount.
    @Query("""
        SELECT new com.harika.inventorymanagementsystem.dto.SupplierOutstandingResponse(
            pi.supplier.partyName,
            SUM(pi.totalAmount) -
            COALESCE(
                (
                    SELECT SUM(sp.amount)
                    FROM SupplierPayment sp
                    WHERE sp.purchaseInvoice.supplier.id = pi.supplier.id
                ),
                0
            )
        )
        FROM PurchaseInvoice pi
        GROUP BY pi.supplier.id, pi.supplier.partyName
        ORDER BY pi.supplier.partyName
        """)
    List<SupplierOutstandingResponse> getSupplierOutstanding();


    // Get total number of purchase invoices.
    @Query("""
        SELECT COUNT(pi.id)
        FROM PurchaseInvoice pi
        """)
    Long getTotalPurchaseInvoiceCount();


    // Get total purchase amount.
    @Query("""
        SELECT COALESCE(SUM(pi.totalAmount), 0)
        FROM PurchaseInvoice pi
        """)
    BigDecimal getTotalPurchaseInvoiceAmount();


    // Get total accepted units, excluding orphan purchase items.
    @Query("""
        SELECT COALESCE(SUM(COALESCE(item.receivedQuantity, 0)), 0)
        FROM PurchaseItem item
        WHERE item.purchaseInvoice IS NOT NULL
        """)
    Long getTotalUnitsPurchased();
}