package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

}