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

@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        orderService.createOrder(request);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Order created successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable String orderId) {
        ApiResponse<OrderResponse> response = ApiResponse.<OrderResponse>builder()
                .data(orderService.getOrderById(orderId))
                .message("Get order successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<Void>> UpdateOrderStatus(
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        orderService.updateOrderStatus(request);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Update order status successfully")
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> filter(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String warehouseId,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse<Page<OrderResponse>> response = ApiResponse.<Page<OrderResponse>>builder()
                .data(orderService.getOrders(warehouseId, status, pageable))
                .message("Get orders successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
