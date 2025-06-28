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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/purchase-orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "PurchaseOrder", description = "APIs for purchase order management.")
public class PurchaseOrderController {

        PurchaseOrderService purchaseOrderService;

        @Operation(summary = "Create purchase order", description = "Register a new purchase order.")
        @PostMapping
        public ResponseEntity<ApiResponse<PurchaseOrderResponse>> create(
                        @Parameter(description = "Purchase order creation request body", required = true)
                        @Valid @RequestBody CreatePurchaseOrderRequest request) {
                ApiResponse<PurchaseOrderResponse> apiResponse = ApiResponse.<PurchaseOrderResponse>builder()
                                .data(purchaseOrderService.createPurchaseOrder(request))
                                .message("Purchase order created successfully")
                                .build();
                return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }

        @Operation(summary = "Get purchase order by ID", description = "Get purchase order details by purchaseOrderId.")
        @GetMapping("/{purchaseOrderId}")
        public ResponseEntity<ApiResponse<PurchaseOrderResponse>> getById(
                        @Parameter(description = "Purchase Order ID", required = true) @PathVariable String purchaseOrderId) {
                ApiResponse<PurchaseOrderResponse> apiResponse = ApiResponse.<PurchaseOrderResponse>builder()
                                .data(purchaseOrderService.getPurchaseOrderById(purchaseOrderId))
                                .message("Get purchase order successfully")
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Update purchase order status", description = "Update status of a purchase order.")
        @PatchMapping("/update-status")
        public ResponseEntity<ApiResponse<Void>> updateStatus(
                        @Parameter(description = "Purchase order status update request body", required = true)
                        @Valid @RequestBody UpdatePurchaseOrderStatusRequest request) {
                purchaseOrderService.updatePurchaseOrderStatus(request);
                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                                .message("Purchase order status updated successfully")
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Get purchase orders", description = "Get purchase orders with filters and pagination.")
        @GetMapping
        public ResponseEntity<ApiResponse<Page<PurchaseOrderResponse>>> getPurchaseOrders(
                        @Parameter(description = "Warehouse ID to filter")
                        @RequestParam(required = false) String warehouseId,
                        @Parameter(description = "Supplier ID to filter")
                        @RequestParam(required = false) String supplierId,
                        @Parameter(description = "Purchase order status to filter")
                        @RequestParam(required = false) PurchaseOrderStatus status,
                        @Parameter(hidden = true) @PageableDefault(page = 0, size = 10, sort = "warehouse.warehouseName", direction = Sort.Direction.ASC) Pageable pageable) {
                ApiResponse<Page<PurchaseOrderResponse>> apiResponse = ApiResponse
                                .<Page<PurchaseOrderResponse>>builder()
                                .data(purchaseOrderService.getPurchaseOrders(warehouseId, supplierId, status, pageable))
                                .message("Get purchase orders successfully")
                                .build();
                return ResponseEntity.ok(apiResponse);
        }


}
