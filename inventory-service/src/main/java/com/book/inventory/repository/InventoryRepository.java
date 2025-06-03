package com.book.inventory.repository;

import com.book.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {

    Optional<Inventory> findByProductIdAndWarehouse_WarehouseId(String productId, String warehouseId);

    List<Inventory> findByWarehouse_WarehouseIdAndAvailableQuantityLessThan(String warehouseId, Integer quantity);

}
