package com.book.inventory.mapper;

import com.book.inventory.dto.request.PurchaseOrderDetailRequest;
import com.book.inventory.entity.Supplier;
import com.book.inventory.dto.response.PurchaseOrderResponse;
import com.book.inventory.entity.Warehouse;
import com.book.inventory.exception.CustomException;
import com.book.inventory.exception.ErrorCode;
import com.book.inventory.repository.PurchaseOrderRepository;
import com.book.inventory.repository.SupplierRepository;
import com.book.inventory.repository.WarehouseRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseOrderMapperHelp {

    SupplierRepository supplierRepository;
    WarehouseRepository warehouseRepository;

    @Named("supplierIdToSupplier")
    public Supplier supplierIdToSupplier(String supplierId) {
        return supplierRepository.findById(supplierId).orElseThrow(
                () -> new CustomException((ErrorCode.SUPPLIER_NOT_FOUND), supplierId));
    }

    @Named("warehouseIdToWarehouse")
    public Warehouse warehouseIdToWarehouse(String warehouseId) {
        return warehouseRepository.findById(warehouseId).orElseThrow(
                () -> new CustomException((ErrorCode.WAREHOUSE_NOT_FOUND), warehouseId));
    }

    @Named("calculateTotalAmount")
    public BigDecimal calculateTotalAmount(List<PurchaseOrderDetailRequest> details) {
        if (details == null)
            return BigDecimal.ZERO;
        return details.stream()
                .map(d -> d.getUnitPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
