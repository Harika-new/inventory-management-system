
package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.dto.SupplierOutstandingResponse;
import com.harika.inventorymanagementsystem.entity.PurchaseInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PurchaseInvoiceRepository
        extends JpaRepository<PurchaseInvoice, Long> {

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

    @Query("SELECT COUNT(pi.id) FROM PurchaseInvoice pi")
    Long getTotalPurchaseInvoiceCount();

    @Query("""
        SELECT COALESCE(SUM(pi.totalAmount), 0)
        FROM PurchaseInvoice pi
        """)
    BigDecimal getTotalPurchaseInvoiceAmount();

    @Query("""
        SELECT COALESCE(SUM(item.quantity), 0)
        FROM PurchaseItem item
        """)
    Long getTotalUnitsPurchased();
}
