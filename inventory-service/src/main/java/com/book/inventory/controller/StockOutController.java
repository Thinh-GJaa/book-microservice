package com.book.inventory.controller;

import com.book.inventory.dto.ApiResponse;
import com.book.inventory.dto.request.CreateStockOutRequest;
import com.book.inventory.dto.request.UpdateStockOutStatusRequest;
import com.book.inventory.dto.response.StockOutResponse;
import com.book.inventory.enums.StockOutStatus;
import com.book.inventory.enums.StockOutType;
import com.book.inventory.service.StockOutService;
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
@RequestMapping("/stock-outs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "StockOut", description = "APIs for stock out management.")
public class StockOutController {

    StockOutService stockOutService;

    @Operation(summary = "Create stock out", description = "Register a new stock out.")
    @PostMapping
    public ResponseEntity<ApiResponse<StockOutResponse>> createStockOut(
            @Parameter(description = "Stock out creation request body", required = true)
            @Valid @RequestBody CreateStockOutRequest request) {
        ApiResponse<StockOutResponse> response = ApiResponse.<StockOutResponse>builder()
                .data(stockOutService.createStockOut(request))
                .message("Stock out created successfully")
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get stock out by ID", description = "Get stock out details by stockOutId.")
    @GetMapping("/{stockOutId}")
    public ResponseEntity<ApiResponse<StockOutResponse>> getStockOutById(
            @Parameter(description = "Stock Out ID", required = true) @PathVariable String stockOutId) {
        ApiResponse<StockOutResponse> response = ApiResponse.<StockOutResponse>builder()
                .data(stockOutService.getStockOutById(stockOutId))
                .message("Get stock out successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update stock out status", description = "Update status of a stock out.")
    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<Void>> updateStockOutStatus(
            @Parameter(description = "Stock out status update request body", required = true)
            @Valid @RequestBody UpdateStockOutStatusRequest request) {
        stockOutService.updateStockOutStatus(request);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Update stock out status successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter stock outs", description = "Filter stock outs by status, type, warehouse and pagination.")
    @GetMapping("/filter")
    public ResponseEntity<ApiResponse<Page<StockOutResponse>>> searchStockOuts(
            @Parameter(description = "Stock out status to filter")
            @RequestParam(required = false) StockOutStatus status,
            @Parameter(description = "Stock out type to filter")
            @RequestParam(required = false) StockOutType type,
            @Parameter(description = "Warehouse ID to filter")
            @RequestParam(required = false) String warehouseId,
            @Parameter(hidden = true) @PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse<Page<StockOutResponse>> response = ApiResponse.<Page<StockOutResponse>>builder()
                .data(stockOutService.filterStockOuts(warehouseId, type, status, pageable))
                .message("Filter stock outs successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
