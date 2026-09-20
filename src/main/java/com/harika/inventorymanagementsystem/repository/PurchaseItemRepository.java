package com.harika.inventorymanagementsystem.repository;

import com.harika.inventorymanagementsystem.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
}