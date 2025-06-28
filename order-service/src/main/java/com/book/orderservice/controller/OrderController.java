package com.book.orderservice.controller;

import com.book.orderservice.dto.ApiResponse;
import com.book.orderservice.dto.request.CreateOrderRequest;
import com.book.orderservice.dto.request.UpdateOrderStatusRequest;
import com.book.orderservice.dto.response.OrderResponse;
import com.book.orderservice.enums.OrderStatus;
import com.book.orderservice.service.OrderService;
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
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Order", description = "APIs for order management.")
public class OrderController {

    OrderService orderService;

    @Operation(summary = "Create order", description = "Create a new order.")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createOrder(
            @Parameter(description = "Order creation request body", required = true)
            @Valid @RequestBody CreateOrderRequest request) {

        orderService.createOrder(request);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Order created successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get order by ID", description = "Retrieve order details by order ID.")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @Parameter(description = "Order ID", example = "7b2a2e3c-9f92-4c73-a8be-1db4d17f3a3c", required = true)
            @PathVariable String orderId) {
        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .data(orderService.getOrderById(orderId))
                .message("Get order successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update order status", description = "Update the status of an order.")
    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<Void>> UpdateOrderStatus(
            @Parameter(description = "Order status update request body", required = true)
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        orderService.updateOrderStatus(request);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Update order status successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter orders", description = "Filter orders by status, warehouse, and pagination.")
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> filter(
            @Parameter(description = "Order status to filter", example = "7b2a2e3c-9f92-4c73-a8be-1db4d17f3a3c", required = false)
            @RequestParam(required = false) OrderStatus status,
            @Parameter(description = "Warehouse ID to filter", example = "7b2a2e3c-9f92-4c73-a8be-1db4d17f3a3c", required = false)
            @RequestParam(required = false) String warehouseId,
            @Parameter(description = "Pagination information (page, size, sort)", example = "page=0&size=10&sort=createdAt,desc", required = false)
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse<Page<OrderResponse>> response = ApiResponse.<Page<OrderResponse>>builder()
                .data(orderService.getOrders(warehouseId, status, pageable))
                .message("Get orders successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
