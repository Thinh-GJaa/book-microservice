package com.book.inventory.dto.response;

import lombok.*;
import com.book.inventory.enums.PurchaseOrderStatus;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseOrderResponse implements Serializable {
    String purchaseOrderId;

    String note;

    SupplierResponse supplier;

    WarehouseResponse warehouse;

    BigDecimal totalAmount;

    PurchaseOrderStatus status;

    List<PurchaseOrderDetailResponse> details;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SupplierResponse implements Serializable{
        String supplierId;
        String supplierName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class WarehouseResponse implements Serializable{
        String warehouseId;
        String warehouseName;
    }

}
