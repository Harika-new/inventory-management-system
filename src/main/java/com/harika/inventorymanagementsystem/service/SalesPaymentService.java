package com.harika.inventorymanagementsystem.service;

import com.harika.inventorymanagementsystem.entity.SalesInvoice;
import com.harika.inventorymanagementsystem.entity.SalesPayment;
import com.harika.inventorymanagementsystem.exception.InvalidPaymentException;
import com.harika.inventorymanagementsystem.repository.SalesInvoiceRepository;
import com.harika.inventorymanagementsystem.repository.SalesPaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SalesPaymentService {

    private final SalesPaymentRepository salesPaymentRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;

    public SalesPaymentService(
            SalesPaymentRepository salesPaymentRepository,
            SalesInvoiceRepository salesInvoiceRepository) {

        this.salesPaymentRepository = salesPaymentRepository;
        this.salesInvoiceRepository = salesInvoiceRepository;
    }

    public BigDecimal getTotalPaidForSalesInvoice(Long salesInvoiceId) {

        List<SalesPayment> payments =
                salesPaymentRepository.findBySalesInvoiceId(salesInvoiceId);

        BigDecimal totalPaid = BigDecimal.ZERO;

        for (SalesPayment payment : payments) {
            totalPaid = totalPaid.add(payment.getAmount());
        }

        return totalPaid;
    }

    public BigDecimal getPendingAmount(Long salesInvoiceId) {

        SalesInvoice salesInvoice =
                salesInvoiceRepository.findById(salesInvoiceId)
                        .orElseThrow(() ->
                                new RuntimeException("Sales Invoice not found"));

        BigDecimal totalPaid =
                getTotalPaidForSalesInvoice(salesInvoiceId);

        return salesInvoice.getTotalAmount().subtract(totalPaid);
    }

    public SalesPayment saveSalesPayment(
            SalesPayment salesPayment) {

        Long salesInvoiceId =
                salesPayment.getSalesInvoice().getId();

        BigDecimal pendingAmount =
                getPendingAmount(salesInvoiceId);

        if (salesPayment.getAmount().compareTo(pendingAmount) > 0) {

            throw new InvalidPaymentException(
                    "Payment cannot be greater than pending amount"
            );
        }

        return salesPaymentRepository.save(salesPayment);
    }

    public List<SalesPayment> getAllSalesPayments() {
        return salesPaymentRepository.findAll();
    }

    public Optional<SalesPayment> getSalesPaymentById(Long id) {
        return salesPaymentRepository.findById(id);
    }

    public void deleteSalesPayment(Long id) {
        salesPaymentRepository.deleteById(id);
    }
}