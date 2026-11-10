
package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.dto.CustomerOutstandingResponse;
import com.harika.inventorymanagementsystem.entity.SalesInvoice;
import com.harika.inventorymanagementsystem.dto.SalesSummaryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SalesInvoiceRepository
        extends JpaRepository<SalesInvoice, Long> {

    @Query("""
        SELECT new com.harika.inventorymanagementsystem.dto.CustomerOutstandingResponse(
            si.customer.name,
            SUM(si.totalAmount) -
            COALESCE(
                (
                    SELECT SUM(sp.amount)
                    FROM SalesPayment sp
                    WHERE sp.salesInvoice.customer.id = si.customer.id
                ),
                0
            )
        )
        FROM SalesInvoice si
        GROUP BY si.customer.id, si.customer.name
        ORDER BY si.customer.name
        """)
    List<CustomerOutstandingResponse> getCustomerOutstanding();


    // Total number of sales invoices.
    @Query("""
        SELECT COUNT(si.id)
        FROM SalesInvoice si
        """)
    Long countTotalBills();


    // Total quantity of products sold.
    @Query("""
        SELECT COALESCE(SUM(item.quantity), 0)
        FROM SalesItem item
        """)
    Long getTotalUnitsSold();


    // Total sales amount.
    // Each invoice is counted only once.
    @Query("""
        SELECT COALESCE(SUM(si.totalAmount), 0)
        FROM SalesInvoice si
        """)
    BigDecimal getTotalSalesAmount();
}
