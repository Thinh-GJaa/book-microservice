package com.book.inventory.repository;

import com.book.inventory.entity.Inventory;
import com.book.inventory.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {

    Optional<Inventory> findByProductIdAndWarehouse_WarehouseId(String productId, String warehouseId);


}
