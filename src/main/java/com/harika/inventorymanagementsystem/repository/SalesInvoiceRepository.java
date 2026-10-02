package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.dto.CustomerOutstandingResponse;
import com.harika.inventorymanagementsystem.dto.SalesSummaryResponse;
import com.harika.inventorymanagementsystem.entity.SalesInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query("""
        SELECT new com.harika.inventorymanagementsystem.dto.SalesSummaryResponse(
            COUNT(DISTINCT si.id),
            SUM(item.quantity),
            SUM(si.totalAmount)
        )
        FROM SalesInvoice si
        JOIN si.salesItems item
        """)
    SalesSummaryResponse getSalesSummary();
}