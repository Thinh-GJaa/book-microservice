package com.book.inventory.controller;

import com.book.inventory.dto.ApiResponse;
import com.book.inventory.dto.request.CreatePurchaseOrderRequest;
import com.book.inventory.dto.request.UpdatePurchaseOrderStatusRequest;
import com.book.inventory.dto.response.PurchaseOrderResponse;
import com.book.inventory.enums.PurchaseOrderStatus;
import com.book.inventory.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase-orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PurchaseOrderController {

        PurchaseOrderService purchaseOrderService;

        @PostMapping
        public ResponseEntity<ApiResponse<PurchaseOrderResponse>> create(
                        @Valid @RequestBody CreatePurchaseOrderRequest request) {
                ApiResponse<PurchaseOrderResponse> apiResponse = ApiResponse.<PurchaseOrderResponse>builder()
                                .data(purchaseOrderService.createPurchaseOrder(request))
                                .message("Purchase order created successfully")
                                .build();
                return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }

        @GetMapping("/{purchaseOrderId}")
        public ResponseEntity<ApiResponse<PurchaseOrderResponse>> getById(@PathVariable String purchaseOrderId) {
                ApiResponse<PurchaseOrderResponse> apiResponse = ApiResponse.<PurchaseOrderResponse>builder()
                                .data(purchaseOrderService.getPurchaseOrderById(purchaseOrderId))
                                .message("Get purchase order successfully")
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @PatchMapping("/update-status")
        public ResponseEntity<ApiResponse<Void>> updateStatus(
                        @Valid @RequestBody UpdatePurchaseOrderStatusRequest request) {
                purchaseOrderService.updatePurchaseOrderStatus(request);
                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                                .message("Purchase order status updated successfully")
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @GetMapping
        public ResponseEntity<ApiResponse<Page<PurchaseOrderResponse>>> getPurchaseOrders(
                        @RequestParam(required = false) String warehouseId,
                        @RequestParam(required = false) String supplierId,
                        @RequestParam(required = false) PurchaseOrderStatus status,
                        @PageableDefault(page = 0, size = 10, sort = "warehouse.warehouseName", direction = Sort.Direction.ASC) Pageable pageable) {
                ApiResponse<Page<PurchaseOrderResponse>> apiResponse = ApiResponse
                                .<Page<PurchaseOrderResponse>>builder()
                                .data(purchaseOrderService.getPurchaseOrders(warehouseId, supplierId, status, pageable))
                                .message("Get purchase orders successfully")
                                .build();
                return ResponseEntity.ok(apiResponse);
        }


}
