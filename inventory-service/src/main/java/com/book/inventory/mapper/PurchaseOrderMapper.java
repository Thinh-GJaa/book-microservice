package com.book.inventory.mapper;

import com.book.inventory.dto.request.*;
import com.book.inventory.dto.response.*;
import com.book.inventory.entity.*;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = { PurchaseOrderMapperHelp.class })
public interface PurchaseOrderMapper {

    @Mapping(target = "supplier", source = "supplierId", qualifiedByName = "supplierIdToSupplier")
    @Mapping(target = "warehouse", source = "warehouseId", qualifiedByName = "warehouseIdToWarehouse")
    @Mapping(target = "totalAmount", source = "details", qualifiedByName = "calculateTotalAmount")
    PurchaseOrder toPurchaseOrder(CreatePurchaseOrderRequest request);

    PurchaseOrderResponse toPurchaseOrderResponse(PurchaseOrder entity);




    List<PurchaseOrderDetail> toPurchaseOrderDetails(List<PurchaseOrderDetailRequest> requests);

    PurchaseOrderDetail toPurchaseOrderDetail(PurchaseOrderDetailRequest request);

    List<PurchaseOrderDetailResponse> toPurchaseOrderDetailResponses(List<PurchaseOrderDetail> details);

    PurchaseOrderDetailResponse toPurchaseOrderDetailResponse(PurchaseOrderDetail detail);

}
