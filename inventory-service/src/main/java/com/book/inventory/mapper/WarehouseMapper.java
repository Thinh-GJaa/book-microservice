package com.book.inventory.mapper;

import com.book.inventory.dto.request.CreateWarehouseRequest;
import com.book.inventory.dto.request.UpdateWarehouseRequest;
import com.book.inventory.dto.response.WarehouseResponse;
import com.book.inventory.entity.Warehouse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    Warehouse toWarehouse(CreateWarehouseRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateWarehouse(@MappingTarget Warehouse warehouse, UpdateWarehouseRequest request);

    WarehouseResponse toWarehouseResponse(Warehouse warehouse);
}
