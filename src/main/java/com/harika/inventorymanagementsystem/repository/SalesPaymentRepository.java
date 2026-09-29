package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.entity.SalesPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesPaymentRepository extends JpaRepository<SalesPayment, Long> {

    List<SalesPayment> findBySalesInvoiceId(Long salesInvoiceId);
}