package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {


}