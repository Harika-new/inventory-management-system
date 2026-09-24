package com.harika.inventorymanagementsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer name does NOT need to be unique.
    // Multiple customers can have the same name.
    private String name;

    // Phone is customer information.
    // We are not making it unique for now.
    private String phone;

    // Generate getters and setters using IntelliJ.
}