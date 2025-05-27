package com.book.inventory.mapper;

import com.book.inventory.dto.request.CreateSupplierRequest;
import com.book.inventory.dto.request.UpdateSupplierRequest;
import com.book.inventory.dto.response.SupplierResponse;
import com.book.inventory.entity.Supplier;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    Supplier toSupplier(CreateSupplierRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSupplier(@MappingTarget Supplier supplier, UpdateSupplierRequest request);

    SupplierResponse toSupplierResponse(Supplier supplier);
}
