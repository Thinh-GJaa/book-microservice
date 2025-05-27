package com.book.inventory.service;

import com.book.inventory.dto.request.CreateWarehouseRequest;
import com.book.inventory.dto.request.UpdateWarehouseRequest;
import com.book.inventory.dto.response.WarehouseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {
    WarehouseResponse createWarehouse(CreateWarehouseRequest request);

    WarehouseResponse getWarehouseById(String warehouseId);

    WarehouseResponse updateWarehouse(UpdateWarehouseRequest request);

    Page<WarehouseResponse> searchWarehouses(String keyword, Pageable pageable);
}
