package com.book.inventory.service;

import com.book.inventory.dto.request.*;
import com.book.inventory.dto.response.*;
import com.book.inventory.enums.PurchaseOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseOrderService {
    PurchaseOrderResponse createPurchaseOrder(CreatePurchaseOrderRequest request);

    PurchaseOrderResponse getPurchaseOrderById(String purchaseOrderId);

    PurchaseOrderResponse updatePurchaseOrderStatus(UpdatePurchaseOrderStatusRequest request);

    Page<PurchaseOrderResponse> getPurchaseOrders(String warehouseId, String supplierId, PurchaseOrderStatus status , Pageable pageable);



}
