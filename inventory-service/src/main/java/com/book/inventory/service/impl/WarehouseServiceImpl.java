package com.book.inventory.service.impl;

import com.book.inventory.dto.request.CreateWarehouseRequest;
import com.book.inventory.dto.request.UpdateWarehouseRequest;
import com.book.inventory.dto.response.WarehouseResponse;
import com.book.inventory.entity.Warehouse;
import com.book.inventory.exception.CustomException;
import com.book.inventory.exception.ErrorCode;
import com.book.inventory.mapper.WarehouseMapper;
import com.book.inventory.repository.WarehouseRepository;
import com.book.inventory.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseServiceImpl implements WarehouseService {

    WarehouseRepository warehouseRepository;
    WarehouseMapper warehouseMapper;

    @Override
    @CacheEvict(value = { "warehouses" }, allEntries = true)
    public WarehouseResponse createWarehouse(CreateWarehouseRequest request) {
        Warehouse warehouse = warehouseMapper.toWarehouse(request);
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toWarehouseResponse(warehouse);
    }

    @Override
    @Cacheable(value = "warehouse", key = "#warehouseId")
    public WarehouseResponse getWarehouseById(String warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new CustomException(ErrorCode.WAREHOUSE_NOT_FOUND, warehouseId));
        return warehouseMapper.toWarehouseResponse(warehouse);
    }

    @Override
    @Caching(put = @CachePut(value = "warehouse", key = "#request.warehouseId"),
            evict = @CacheEvict(value = {"warehouses" }, allEntries = true))
    public WarehouseResponse updateWarehouse(UpdateWarehouseRequest request) {
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new CustomException(ErrorCode.WAREHOUSE_NOT_FOUND, request.getWarehouseId()));
        warehouseMapper.updateWarehouse(warehouse, request);
        warehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toWarehouseResponse(warehouse);
    }

    @Override
    @Cacheable(value = "warehouses", key = "#keyword + '_' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()", condition = "#pageable.pageNumber < 2", unless = "#result.isEmpty()")
    public Page<WarehouseResponse> searchWarehouses(String keyword, Pageable pageable) {
        Page<Warehouse> warehouses = warehouseRepository
                .findByWarehouseNameContainingIgnoreCaseOrAddressContainingIgnoreCase(
                        keyword, keyword, pageable);
        return warehouses.map(warehouseMapper::toWarehouseResponse);
    }
}
