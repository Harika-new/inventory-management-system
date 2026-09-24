package com.harika.inventorymanagementsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sales_invoice")
public class SalesInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SalesItem> getSalesItems() {
        return salesItems;
    }

    public void setSalesItems(List<SalesItem> salesItems) {
        this.salesItems = salesItems;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Bill number written/generated for the customer's sale.
    private String billNumber;

    // Date on which the sale happened.
    private LocalDate date;

    // One Customer can have many SalesInvoices.
    // Each SalesInvoice belongs to one Customer.
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // One SalesInvoice can contain many SalesItems.
    @OneToMany(mappedBy = "salesInvoice", cascade = CascadeType.ALL)
    private List<SalesItem> salesItems;

    // Total amount of the complete bill.
    private BigDecimal totalAmount;

    // Generate getters and setters using IntelliJ.
}