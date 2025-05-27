package com.book.inventory.repository;

import com.book.inventory.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {



    Page<Warehouse> findByWarehouseNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
            String warehouseName, String address, Pageable pageable);
}
