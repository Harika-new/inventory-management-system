package com.harika.inventorymanagementsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
// Purpose:
// Tells JPA that Product is an entity.
// Hibernate will map this Java class to a database table.

@Table(name = "product")
// Purpose:
// Explicitly tells Hibernate which database table this entity maps to.
// Product Java class → product database table.

public class Product {

    /*
     * Product fields
     *
     * id           → Unique identifier
     * name         → Product name
     * category     → Product category
     * type         → Product type/style
     * sellingPrice → Current selling price
     * size         → Product size
     */

    @Id
    // Purpose:
    // Tells JPA that id is the PRIMARY KEY of the product table.

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Purpose:
    // Tells Hibernate to generate the id automatically.
    // IDENTITY means the database generates the next ID value.

    private Long id;


    @Column(name = "product_name")
    // Purpose:
    // Controls how this Java field is mapped to the database column.
    // Java field: name → Database column: product_name

    private String name;


    private String category;
    // No @Column here:
    // Hibernate uses the default mapping.
    // Java field: category → Database column: category.


    private String type;
    // Default mapping:
    // Java field: type → Database column: type.


    // BigDecimal is preferred for money because it provides
    // accurate decimal arithmetic compared with float/double.
    private BigDecimal sellingPrice;


    private String size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    // String is suitable because sizes can contain letters/numbers:
    // M, L, XL, 2XL, 75, 80, etc.
}