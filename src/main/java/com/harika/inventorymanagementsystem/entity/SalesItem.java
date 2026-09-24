package com.harika.inventorymanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sales_item")
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public SalesInvoice getSalesInvoice() {
        return salesInvoice;
    }

    public void setSalesInvoice(SalesInvoice salesInvoice) {
        this.salesInvoice = salesInvoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    // Prevent circular JSON:
    // SalesInvoice → SalesItems → SalesInvoice → ...
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sales_invoice_id")
    private SalesInvoice salesInvoice;

    // The Product being sold.
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Number of units sold.
    private Integer quantity;

    // Selling price before customer discount.
    private BigDecimal sellingPrice;

    // Discount given for this particular sale.
    // This is editable during billing.
    private BigDecimal discount;

    // Final price after discount.
    private BigDecimal finalPrice;

    // Generate getters and setters using IntelliJ.
}