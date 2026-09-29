package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.entity.SupplierPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierPaymentRepository
        extends JpaRepository<SupplierPayment, Long> {

    List<SupplierPayment> findByPurchaseInvoiceId(Long purchaseInvoiceId);


}